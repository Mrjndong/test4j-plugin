package org.test4j.plugin.jspec.assistor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.jface.text.reconciler.IReconcilingStrategyExtension;
import org.eclipse.swt.widgets.Display;
import org.test4j.plugin.jspec.ui.JSpecEditor;
import org.test4j.spec.scenario.step.txt.LineType;
import org.test4j.tools.commons.StringHelper;

public class JSpecReconcilingStrategy implements IReconcilingStrategy, IReconcilingStrategyExtension {
	private JSpecEditor editor;

	private IDocument fDocument;

	/** holds the calculated positions */
	protected final List<Position> fPositions = new ArrayList<Position>();

	/** The end offset of the range to be scanned */
	protected int lines;

	protected void calculatePositions() {
		fPositions.clear();
		// cNextPos = fOffset;

		try {
			recursiveTokens(0);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}

		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				editor.updateFoldingStructure(fPositions);
				editor.updateOutlinePage();
			}
		});
	}

	private void recursiveTokens(int i) throws BadLocationException {
		int scenarioOffset = 0;
		int stepOffset = -1;
		int length = 0;
		this.fPositions.clear();
		for (int index = 0; index < this.lines; index++) {
			int line_offset = this.fDocument.getLineOffset(index);
			int line_length = this.fDocument.getLineLength(index);
			String line = this.fDocument.get(line_offset, line_length);

			LineType type = LineType.getLineType(line);
			switch (type) {
			case Scenario:
			case SkipScenario:
				if (stepOffset > -1) {
					this.fPositions.add(new Position(stepOffset, length - stepOffset));
				}
				if (length > scenarioOffset) {
					this.fPositions.add(new Position(scenarioOffset, length - scenarioOffset));
				}
				scenarioOffset = line_offset;
				stepOffset = -1;
				break;
			case Given:
			case SkipGiven:
			case When:
			case SkipWhen:
			case Then:
			case SkipThen:
				if (stepOffset > -1) {
					this.fPositions.add(new Position(stepOffset, length - stepOffset));
				}
				stepOffset = line_offset;
				break;
			default:
			}

			if (!StringHelper.isBlankOrNull(line)) {
				length = line_offset + line_length;
			}
		}
		if (stepOffset > -1) {
			this.fPositions.add(new Position(stepOffset, length - stepOffset));
		}
		if (length > scenarioOffset) {
			this.fPositions.add(new Position(scenarioOffset, length - scenarioOffset));
		}
	}

	public void setProgressMonitor(IProgressMonitor monitor) {
	}

	public void initialReconcile() {
		this.lines = fDocument.getNumberOfLines();
		calculatePositions();
	}

	public void setDocument(IDocument document) {
		this.fDocument = document;
	}

	public void reconcile(DirtyRegion dirtyRegion, IRegion subRegion) {
		initialReconcile();
	}

	public void reconcile(IRegion partition) {
		initialReconcile();
	}

	public JSpecEditor getEditor() {
		return editor;
	}

	public void setEditor(JSpecEditor editor) {
		this.editor = editor;
	}
}
