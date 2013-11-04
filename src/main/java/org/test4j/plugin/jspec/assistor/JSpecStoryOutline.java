package org.test4j.plugin.jspec.assistor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.DefaultPositionUpdater;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IPositionUpdater;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;
import org.test4j.spec.scenario.step.txt.LineType;

public class JSpecStoryOutline extends ContentOutlinePage {
	protected IEditorInput fInput;
	protected IDocumentProvider fDocumentProvider;
	protected ITextEditor fTextEditor;

	public JSpecStoryOutline(IDocumentProvider provider, ITextEditor editor) {
		this.fDocumentProvider = provider;
		this.fTextEditor = editor;
	}

	public void createControl(Composite parent) {
		super.createControl(parent);

		TreeViewer viewer = getTreeViewer();
		viewer.setContentProvider(new JSpecOutlineContentProvider());
		viewer.setLabelProvider(new LabelProvider());
		viewer.addSelectionChangedListener(this);
		viewer.setInput(this.fInput);
	}

	public void setInput(IEditorInput input) {
		this.fInput = input;
	}

	public void selectionChanged(SelectionChangedEvent event) {
		super.selectionChanged(event);

		ISelection selection = event.getSelection();
		if (selection.isEmpty()) {
			this.fTextEditor.resetHighlightRange();
		} else {
			Segment segment = (Segment) ((IStructuredSelection) selection).getFirstElement();
			int start = segment.position.getOffset();
			int length = segment.position.getLength();
			try {
				this.fTextEditor.setHighlightRange(start, length, true);
			} catch (IllegalArgumentException localIllegalArgumentException) {
				this.fTextEditor.resetHighlightRange();
			}
		}
	}

	public void updateOutline() {
		if (this.fInput == null) {
			return;
		}
		TreeViewer viewer = getTreeViewer();
		if (viewer == null) {
			return;
		}
		Control control = viewer.getControl();
		if (control == null || control.isDisposed()) {
			return;
		} else {
			control.setRedraw(false);
			viewer.setInput(this.fInput);
			viewer.expandAll();
			control.setRedraw(true);
		}
	}

	protected class JSpecOutlineContentProvider implements ITreeContentProvider {
		protected static final String SEGMENTS = "__java_segments";
		protected IPositionUpdater fPositionUpdater = new DefaultPositionUpdater(SEGMENTS);
		protected List<Segment> fContent = new ArrayList<Segment>(20);

		protected JSpecOutlineContentProvider() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			if (oldInput != null) {
				IDocument document = JSpecStoryOutline.this.fDocumentProvider.getDocument(oldInput);
				if (document != null) {
					try {
						document.removePositionCategory(SEGMENTS);
					} catch (BadPositionCategoryException localBadPositionCategoryException) {
					}
					document.removePositionUpdater(this.fPositionUpdater);
				}
			}

			this.fContent.clear();

			if (newInput != null) {
				IDocument document = JSpecStoryOutline.this.fDocumentProvider.getDocument(newInput);
				if (document != null) {
					document.addPositionCategory(SEGMENTS);
					document.addPositionUpdater(this.fPositionUpdater);

					parseOutlineContent(document);
				}
			}
		}

		private void parseOutlineContent(IDocument document) {
			int lines = document.getNumberOfLines();

			Segment scenario = null;
			for (int index = 0; index < lines; index++) {
				try {
					int offset = document.getLineOffset(index);
					int length = document.getLineLength(index);

					String line = document.get(offset, length);
					LineType type = LineType.getLineType(line);
					switch (type) {
					case Scenario:
					case SkipScenario:
						if (scenario != null) {
							this.fContent.add(scenario);
						}
						Position p1 = new Position(offset, length);
						document.addPosition(SEGMENTS, p1);
						scenario = new Segment(line, p1, null);
						break;
					case Given:
					case SkipGiven:
					case When:
					case SkipWhen:
					case Then:
					case SkipThen:
						Position p2 = new Position(offset, length);
						document.addPosition(SEGMENTS, p2);
						if (scenario == null) {
							scenario = new Segment("Scenario default", p2, null);
						}
						scenario.addChild(new Segment(line, p2, scenario));
						break;
					default:
					}
				} catch (BadPositionCategoryException localBadPositionCategoryException) {
				} catch (BadLocationException localBadLocationException) {
				}
			}
			if (scenario != null) {
				this.fContent.add(scenario);
			}
		}

		public void dispose() {
			if (this.fContent != null) {
				this.fContent.clear();
				this.fContent = null;
			}
		}

		public boolean isDeleted(Object element) {
			return false;
		}

		public Object[] getElements(Object element) {
			return this.fContent.toArray();
		}

		public boolean hasChildren(Object element) {
			if (element == JSpecStoryOutline.this.fInput) {
				return true;
			} else if (element instanceof Segment) {
				return ((Segment) element).hasChild();
			} else {
				return false;
			}
		}

		public Object getParent(Object element) {
			if ((element instanceof Segment)) {
				Segment parent = ((Segment) element).getParent();
				return parent == null ? JSpecStoryOutline.this.fInput : parent;
			} else {
				return null;
			}
		}

		public Object[] getChildren(Object element) {
			if (element == JSpecStoryOutline.this.fInput) {
				return this.fContent.toArray();
			} else if (element instanceof Segment) {
				List<Segment> children = ((Segment) element).getChildren();
				return children == null ? new Object[0] : children.toArray();
			} else {
				return new Object[0];
			}
		}
	}

	protected static class Segment {
		public String name;

		public Position position;

		private Segment parent;

		private List<Segment> children;

		public Segment(String name, Position position, Segment parent) {
			this.name = name;
			this.position = position;
			this.parent = parent;
			this.children = new ArrayList<Segment>();
		}

		public String toString() {
			return this.name;
		}

		public void addChild(Segment segment) {
			this.children.add(segment);
		}

		public boolean hasChild() {
			return this.children != null && this.children.size() > 0;
		}

		public List<Segment> getChildren() {
			return this.children;
		}

		public Segment getParent() {
			return parent;
		}
	}
}