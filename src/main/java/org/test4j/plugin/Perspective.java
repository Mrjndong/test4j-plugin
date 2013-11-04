package org.test4j.plugin;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.test4j.plugin.database.ui.DataSetView;
import org.test4j.plugin.database.ui.DatabaseView;
import org.test4j.plugin.database.ui.MessageView;

/**
 * Insert the type's description here.
 * 
 * @see IPerspectiveFactory
 */
public class Perspective implements IPerspectiveFactory {
	/**
	 * The constructor.
	 */
	public Perspective() {
	}

	/**
	 * Insert the method's description here.
	 * 
	 * @see IPerspectiveFactory#createInitialLayout
	 */
	@SuppressWarnings("deprecation")
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();

		// Left folder
		IFolderLayout left = layout.createFolder("left", IPageLayout.LEFT, 0.25f, editorArea);
		left.addView(DatabaseView.ID);
		left.addView(IPageLayout.ID_RES_NAV);

		IFolderLayout bottomRight = layout.createFolder("bottomRight", IPageLayout.BOTTOM, 0.50f, editorArea);
		bottomRight.addView(DataSetView.ID);
		bottomRight.addView(MessageView.ID);
	}
}
