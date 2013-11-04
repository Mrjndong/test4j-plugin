package org.test4j.plugin.jspec.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.test4j.plugin.jspec.ui.JSpecEditor;

public class MarkExpandedHandler extends MarkCollapsedHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		JSpecEditor editor = this.currentEditor(event);
		if (editor == null) {
			return null;
		}
		editor.toggleFolding(false);
		return null;
	}
}
