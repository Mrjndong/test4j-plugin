package org.jtester.wiki.ui.text.region;

import static org.jtester.plugin.helper.PluginSetting.WIKI_FILE_EXTENSION;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.jtester.JTesterActivator;
import org.jtester.plugin.helper.PluginLogger;
import org.jtester.plugin.resources.IconResources;
import org.jtester.wiki.ui.EditedWikiPage;

public class TextRegionVisitor {
	private List<ICompletionProposal> defaultReturnValue;
	private final TextRegion textRegion;
	private final int documentOffset;
	private final EditedWikiPage wikiEditor;

	public TextRegionVisitor(EditedWikiPage wikiEditor, TextRegion paramTextRegion, int paramInt) {
		this.defaultReturnValue = new ArrayList<ICompletionProposal>();
		this.textRegion = paramTextRegion;
		this.documentOffset = paramInt;
		this.wikiEditor = wikiEditor;
	}

	public List<ICompletionProposal> visit(BasicTextRegion basicTextRegion) {
		return getPotentialWikiNameCompletion(this.textRegion.getTextToCursor(), this.documentOffset);
	}

	public List<ICompletionProposal> visit(JavaTypeTextRegion region) {
		return this.defaultReturnValue;
	}

	public List<ICompletionProposal> visit(UrlTextRegion urlTextRegion) {
		return this.defaultReturnValue;
	}

	public List<ICompletionProposal> visit(DbFitTextRegion eclipseResourceTextRegion) {
		int colon = this.textRegion.getTextToCursor().indexOf(":") + 1;
		String location = new String(this.textRegion.getTextToCursor().substring(colon));
		return getResourceCompletions(this.textRegion.getTextToCursor(), location, this.documentOffset);
	}

	private ArrayList<ICompletionProposal> getResourceCompletions(String text, String location, int documentOffset) {
		try {
			int lengthToBeReplaced = 0;
			int replacementOffset = documentOffset;
			int colon = text.indexOf(":");
			int lastSlash = text.lastIndexOf("/");
			int lastSegment = (colon > lastSlash) ? colon : lastSlash;
			if (lastSegment != -1) {
				lengthToBeReplaced = text.length() - lastSegment - 1;
				replacementOffset = documentOffset - lengthToBeReplaced;
			}

			String[] children = getChildren(location);
			return buildResourceProposals(children, "", replacementOffset, lengthToBeReplaced);
		} catch (Exception e) {
			PluginLogger.log("Completion Error", e.getLocalizedMessage(), e);
		}
		return new ArrayList<ICompletionProposal>();
	}

	ArrayList<ICompletionProposal> buildResourceProposals(String[] replacements, String prefix, int replacementOffset,
			int replacementLength) {
		ArrayList<ICompletionProposal> list = new ArrayList<ICompletionProposal>();
		for (String element : replacements) {
			String child = prefix + element;
			list.add(new CompletionProposal(child, replacementOffset, replacementLength, child.length(), IconResources
					.getImage(IconResources.RESOUCE_PERSPECT_ICON), null, null, null));
		}
		return list;
	}

	String[] getChildren(String path) throws CoreException {
		if ((path.length() == 0) || (path.equals("/")) || (path.indexOf("/", 1) == -1)) {
			return getProjectList(path);
		}

		IPath relPath = new Path(path);
		if (relPath.hasTrailingSeparator()) {
			return getChildren(relPath, "");
		}

		String lastBit = relPath.lastSegment();
		relPath = relPath.removeLastSegments(1);
		return getChildren(relPath, lastBit);
	}

	private String[] getChildren(IPath parent, String resourcePrefix) throws CoreException {
		IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember(parent);
		if (resource == null) {
			File xfile = parent.toFile();
			if (!(xfile.exists())) {
				return new String[0];
			}
			return getChildren(resourcePrefix, xfile);
		}
		if ((resource.exists()) && (((resource.getType() == 2) || (resource.getType() == 4)))) {
			return getChildren(resourcePrefix, resource);
		}
		return new String[0];
	}

	private String[] getChildren(String resourcePrefix, File parent) {
		SortedMap<String, String> sort = new TreeMap<String, String>();
		File[] files = parent.listFiles();
		for (File file : files) {
			if (file.getName().startsWith(resourcePrefix)) {
				sort.put(file.getName(), file.getName());
			}
		}
		Collection<String> values = sort.values();
		return values.toArray(new String[values.size()]);
	}

	private String[] getChildren(String resourcePrefix, IResource resource) throws CoreException {
		IContainer container = (IContainer) resource;
		IResource[] children = container.members();
		ArrayList<String> childNames = new ArrayList<String>();
		for (IResource element : children) {
			if (element.getName().startsWith(resourcePrefix)) {
				childNames.add(element.getName());
			}
		}
		return childNames.toArray(new String[childNames.size()]);
	}

	String[] getProjectList(String text) {
		String prefix = text;
		if ((prefix != null) && (prefix.startsWith("/"))) {
			prefix = new String(prefix.substring(1));
		}
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		ArrayList<String> names = new ArrayList<String>();
		for (IProject proj : projects) {
			if ((prefix == null) || (prefix.length() == 0) || (proj.getName().startsWith(prefix))) {
				names.add(proj.getName());
			}
		}
		return names.toArray(new String[names.size()]);
	}

	private ArrayList<ICompletionProposal> getPotentialWikiNameCompletion(String text, int documentOffset) {
		String word = text.trim();
		try {
			if ((word.length() > 0) && (!(Character.isUpperCase(word.charAt(0))))) {
				return new ArrayList<ICompletionProposal>();
			}
			IResource[] resources = this.wikiEditor.getContext().getWorkingLocation().members(1);
			ArrayList<ICompletionProposal> list = new ArrayList<ICompletionProposal>();
			for (IResource element : resources) {
				String name = element.getName();
				if ((isWikiFile(name)) && (name.startsWith(word))) {
					String wikiName = getWikiWord(name);
					ICompletionProposal proposal = new CompletionProposal(wikiName, documentOffset - word.length(),
							word.length(), wikiName.length(), JTesterActivator.getDefault().getImageRegistry()
									.get("icons/wiki.png"), null, null, null);
					list.add(proposal);
				}
			}
			return list;
		} catch (CoreException e) {
			PluginLogger.log("Completion Error", e.getLocalizedMessage(), e);
		}
		return new ArrayList<ICompletionProposal>();
	}

	private String getWikiWord(String fileName) {
		return new String(fileName.substring(0, fileName.indexOf(WIKI_FILE_EXTENSION)));
	}

	private boolean isWikiFile(String name) {
		return name.endsWith(WIKI_FILE_EXTENSION);
	}
}