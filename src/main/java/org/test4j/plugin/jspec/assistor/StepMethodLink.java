package org.test4j.plugin.jspec.assistor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.test4j.plugin.jspec.JSpecProp;
import org.test4j.spec.inner.StepType;
import org.test4j.spec.scenario.step.txt.LineType;

public class StepMethodLink implements IHyperlink {
	final IDocument document;
	private int lineOffset;
	private int lineLength;

	// private final String line;
	private final String stepName;
	private final StepType gwzType;
	private int lineNo;

	public StepMethodLink(IDocument document, String line, int lineNo, int lineOffset, int lineLength) {
		this.document = document;
		this.lineNo = lineNo;
		this.lineOffset = lineOffset;
		this.lineLength = lineLength;
		LineType type = LineType.getLineType(line);
		this.gwzType = type.getStepType();
		this.stepName = type.getSurfixText(line);
	}

	public IRegion getHyperlinkRegion() {
		return new Region(lineOffset, this.lineLength);
	}

	public String getHyperlinkText() {
		return this.stepName;
	}

	public String getTypeLabel() {
		return "Go to step Method";
	}

	public void open() {
		try {
			JSpecFastPartitioner partitioner = (JSpecFastPartitioner) document.getDocumentPartitioner();

			IProject project = partitioner.getProject();
			if (project == null) {
				new JSpecInfoDialog().show(JSpecProp.CANNOT_LOCATE_STEP_TITLE, JSpecProp.CANNOT_LOCATE_STEP_MESSAGE);
				return;
			}
			IFile ifile = this.getCurrentFile();
			String[] args = this.getArgNames();

			IJavaElement je = StepMethodLocator.findMethod(gwzType, this.stepName, args, ifile);
			if (je != null) {
				JavaUI.openInEditor(je);
				je = null;
			} else {
				new JSpecInfoDialog().show(JSpecProp.STEP_NOT_FOUND_TITLE, JSpecProp.STEP_NOT_FOUND_MESSAGE
						+ this.stepName);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	IFile getCurrentFile() {
		IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if (editorPart == null) {
			return null;
		}
		IFileEditorInput input = (IFileEditorInput) editorPart.getEditorInput();
		if (input == null) {
			return null;
		}
		return input.getFile();
	}

	/**
	 * 获取变量名称
	 * 
	 * @return
	 */
	String[] getArgNames() {
		StringBuilder buff = new StringBuilder();
		try {
			for (int index = this.lineNo + 1; index < this.document.getNumberOfLines(); index++) {
				String line = getDocumentLine(this.document, index);
				LineType type = LineType.getLineType(line);
				if (type == LineType.TextLine) {
					buff.append(line).append("\n");
				} else {
					break;
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		char[] chars = buff.append('\0').toString().toCharArray();
		String[] args = new ArgsParser(chars).getArgs();
		return args;
	}

	private static String getDocumentLine(IDocument document, int index) throws BadLocationException {
		int length = document.getLineLength(index);
		int offset = document.getLineOffset(index);
		String line = document.get(offset, length);
		return line;
	}

	static class ArgsParser {
		private int index;

		private final char[] chars;

		public ArgsParser(char[] chars) {
			this.chars = chars;
			this.index = 0;
		}

		/**
		 * 是否是变量起始符号?
		 * 
		 * @return
		 */
		boolean isArgStart() {
			char ch = chars[index];
			index++;
			if (ch == '\\') {
				index++;
				return false;
			}
			return ch == JSpecProp.STEP_PARA_START_CHAR;
		}

		/**
		 * 是否是变量结束符号?
		 * 
		 * @return
		 */
		boolean isArgEnd() {
			char ch = chars[index];
			index++;
			if (ch == '\\') {
				index++;
				return false;
			}
			return ch == JSpecProp.STEP_PARA_END_CHAR;
		}

		public String[] getArgs() {
			List<String> args = new ArrayList<String>();
			while (index < chars.length) {
				boolean isStart = this.isArgStart();
				if (isStart) {
					String defaultName = String.format("arg%d", args.size() + 1);
					String name = this.getArgName(defaultName);
					args.add(name);
				}
			}

			return args.toArray(new String[0]);
		}

		/**
		 * 解析当前的变量名称
		 * 
		 * @param defaultName
		 *            默认的变量名称
		 * @return
		 */
		String getArgName(String defaultName) {
			StringBuilder buff = new StringBuilder();
			for (; index < chars.length; index++) {
				char ch = chars[index];
				// 反义符
				if (ch == '=') {
					return buff.toString();
				}
				if (ch == JSpecProp.STEP_PARA_END_CHAR || ch == '\\') {
					index++;
					return defaultName;
				}
				if (JSpecProp.ILLEGAL_PARA_CHARS.contains(ch)) {
					return defaultName;
				} else {
					buff.append(ch);
				}
			}
			return defaultName;
		}
	}
}

class NoLink implements IHyperlink {
	public IRegion getHyperlinkRegion() {
		return new Region(0, 0);
	}

	public String getTypeLabel() {
		return "";
	}

	public String getHyperlinkText() {
		return "";
	}

	public void open() {
	}
}
