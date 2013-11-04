package org.jtester.wiki.listener;

public abstract interface PropertyListener {
	/**
	 * 下列2个事件激活事件通知<br>
	 * o org.eclipse.jface.util.PropertyChangeEvent <br>
	 * o java.beans.PropertyChangeEvent
	 */
	public abstract void propertyChanged();
}