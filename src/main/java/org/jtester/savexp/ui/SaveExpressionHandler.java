package org.jtester.savexp.ui;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.debug.core.model.IWatchExpression;
import org.jtester.plugin.helper.PluginLogger;
import org.jtester.savexp.assistor.SaveExpHelper;

public class SaveExpressionHandler extends AbstractHandler {
	protected String javaFilePath = "";

	public Object execute(ExecutionEvent event) throws ExecutionException {
		IEvaluationContext evaluation = (IEvaluationContext) event.getApplicationContext();
		List<?> expressions = (List<?>) evaluation.getDefaultVariable();

		for (Object expression : expressions) {
			String text = null;
			IValue value = null;
			if (expression instanceof IWatchExpression) {
				IWatchExpression watch = (IWatchExpression) expression;
				text = watch.getExpressionText();
				value = watch.getValue();
			} else if (expression instanceof IVariable) {
				try {
					text = ((IVariable) expression).getName();
					value = ((IVariable) expression).getValue();
				} catch (DebugException e) {
					PluginLogger.log(e);
					throw new RuntimeException(e);
				}
			} else {
				PluginLogger.log(expression.getClass().getName());
			}

			this.javaFilePath = SaveExpHelper.getSavePath(value);

			if (text != null && value != null) {
				String filename = SaveExpHelper.getXmlFile(this.javaFilePath, text);
				PluginLogger.log("begin save expression:" + filename);
				SaveExpHelper.toXML(value, filename);
			} else {
				PluginLogger.log("save expression error:" + expression.getClass().getName());
			}
		}
		return null;
	}
}
