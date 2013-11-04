package org.jtester.plugin.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.jtester.database.ui.MessageView;

/**
 * 清除Message Viewer中的消息
 * 
 * @author darui.wudr
 * 
 */
public class ClearMessageHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		MessageView view = MessageView.getInstance();
		view.clearMessages();

		return null;
	}
}
