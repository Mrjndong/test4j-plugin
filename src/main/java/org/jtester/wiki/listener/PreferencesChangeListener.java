package org.jtester.wiki.listener;

import java.beans.PropertyChangeListener;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.jtester.JTesterActivator;

/**
 * wiki参数设置改变监听器
 * 
 * @author darui.wudr
 * 
 */
public class PreferencesChangeListener implements PropertyChangeListener, IPropertyChangeListener {
	private final PropertyListener listener;

	public PreferencesChangeListener(PropertyListener listener) {
		this.listener = listener;
		JTesterActivator.preference().addPropertyChangeListener(this);
	}

	public void dispose() {
		JTesterActivator.preference().removePropertyChangeListener(this);
	}

	public void propertyChange(org.eclipse.jface.util.PropertyChangeEvent event) {
		this.listener.propertyChanged();
	}

	public void propertyChange(java.beans.PropertyChangeEvent evt) {
		this.listener.propertyChanged();
	}
}