package org.test4j.plugin.jspec.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.projection.ProjectionAnnotation;
import org.eclipse.jface.text.source.projection.ProjectionAnnotationModel;
import org.eclipse.jface.text.source.projection.ProjectionSupport;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.test4j.plugin.helper.ColourManager;
import org.test4j.plugin.jspec.assistor.JSpecConfiguration;
import org.test4j.plugin.jspec.assistor.JSpecDocumentProvider;
import org.test4j.plugin.jspec.assistor.JSpecStoryOutline;

@SuppressWarnings("rawtypes")
public class JSpecEditor extends TextEditor {
    public static String      ID      = "org.test4j.plugin.jspec.ui.JSpecEditor";
    public static String      MENU_ID = "#" + ID;

    private ColourManager     colourManager;

    private JSpecStoryOutline fOutlinePage;

    private ProjectionSupport projectionSupport;

    public JSpecEditor() {
        super();
        this.colourManager = new ColourManager();
        setSourceViewerConfiguration(new JSpecConfiguration(colourManager, this));
        setDocumentProvider(new JSpecDocumentProvider());
        this.setEditorContextMenuId(MENU_ID);
    }

    public Object getAdapter(Class adapter) {
        if (IContentOutlinePage.class.equals(adapter)) {
            return getJSpecOutline();
        } else {
            return super.getAdapter(adapter);
        }
    }

    private Object getJSpecOutline() {
        if (this.fOutlinePage == null) {
            this.fOutlinePage = new JSpecStoryOutline(this.getDocumentProvider(), this);
            this.fOutlinePage.setInput(getEditorInput());
        }
        return this.fOutlinePage;
    }

    private ProjectionAnnotationModel annotationModel;

    @Override
    public void createPartControl(Composite parent) {
        super.createPartControl(parent);
        ProjectionViewer viewer = (ProjectionViewer) getSourceViewer();

        projectionSupport = new ProjectionSupport(viewer, getAnnotationAccess(), getSharedColors());
        projectionSupport.install();

        // turn projection mode on
        viewer.doOperation(ProjectionViewer.TOGGLE);
        annotationModel = viewer.getProjectionAnnotationModel();
    }

    @Override
    protected void editorContextMenuAboutToShow(final IMenuManager menuMgr) {
        menuMgr.setRemoveAllWhenShown(true);

        menuMgr.add(new Separator("jspec"));
        menuMgr.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));

        super.editorContextMenuAboutToShow(menuMgr);
    }

    @Override
    protected ISourceViewer createSourceViewer(Composite parent, IVerticalRuler ruler, int styles) {
        // ISourceViewer viewer = super.createSourceViewer(parent, ruler,
        // styles);
        ISourceViewer viewer = new ProjectionViewer(parent, ruler, getOverviewRuler(), isOverviewRulerVisible(), styles);
        // ensure decoration support has been created and configured.
        getSourceViewerDecorationSupport(viewer);

        return viewer;
    }

    private Map<ProjectionAnnotation, Position> foldingAnnotations;

    public void updateFoldingStructure(List<Position> positions) {
        List<Annotation> annotations = new ArrayList<Annotation>();

        // this will hold the new annotations along
        // with their corresponding positions
        Map<ProjectionAnnotation, Position> newAnnotations = new HashMap<ProjectionAnnotation, Position>();
        for (Position position : positions) {
            ProjectionAnnotation annotation = new ProjectionAnnotation();
            if (this.isCollapsed(position)) {
                annotation.markCollapsed();
            }
            newAnnotations.put(annotation, position);
            annotations.add(annotation);
        }

        Annotation[] oldFolding = foldingAnnotations == null ? new Annotation[0] : foldingAnnotations.keySet().toArray(
                new Annotation[0]);
        annotationModel.modifyAnnotations(oldFolding, newAnnotations, null);
        this.foldingAnnotations = newAnnotations;
    }

    public void toggleFolding(boolean collapse) {
        if (this.foldingAnnotations == null) {
            return;
        }
        for (Map.Entry<ProjectionAnnotation, Position> entry : this.foldingAnnotations.entrySet()) {
            ProjectionAnnotation annotation = entry.getKey();
            if (collapse) {
                annotation.markCollapsed();
            } else {
                annotation.markExpanded();
            }
        }
        Annotation[] oldFolding = foldingAnnotations == null ? new Annotation[0] : foldingAnnotations.keySet().toArray(
                new Annotation[0]);
        annotationModel.modifyAnnotations(oldFolding, this.foldingAnnotations, null);
    }

    private boolean isCollapsed(Position position) {
        if (this.foldingAnnotations == null) {
            return false;
        }
        for (Map.Entry<ProjectionAnnotation, Position> entry : this.foldingAnnotations.entrySet()) {
            Position value = entry.getValue();
            if (position.equals(value)) {
                return entry.getKey().isCollapsed();
            }
        }
        return false;
    }

    public void updateOutlinePage() {
        this.fOutlinePage.updateOutline();
    }

    public void dispose() {
        colourManager.dispose();
        super.dispose();
    }

    /**
     * 获得编辑器中的文本内容
     * 
     * @return
     */
    public String getDocumentText() {
        ISourceViewer viewer = super.getSourceViewer();
        if (viewer != null && viewer.getDocument() != null) {
            return viewer.getDocument().get();
        } else {
            return "";
        }
    }

}
