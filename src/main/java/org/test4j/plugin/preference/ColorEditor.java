package org.test4j.plugin.preference;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

public final class ColorEditor {
	private Point fExtent;
	private Image fImage;
	private RGB fColorValue;
	private Color fColor;
	private Button fButton;

	public ColorEditor(Composite parent) {
		this.fButton = new Button(parent, 8);
		this.fExtent = computeImageSize(parent);
		this.fImage = new Image(parent.getDisplay(), this.fExtent.x, this.fExtent.y);

		GC gc = new GC(this.fImage);
		gc.setBackground(this.fButton.getBackground());
		gc.fillRectangle(0, 0, this.fExtent.x, this.fExtent.y);
		gc.dispose();

		this.fButton.setImage(this.fImage);
		this.fButton.addSelectionListener(new ColourSelectionAdapter());
		this.fButton.addDisposeListener(new ColourDisposeListener());
	}

	public RGB getColorValue() {
		return this.fColorValue;
	}

	public void setColorValue(RGB rgb) {
		this.fColorValue = rgb;
		updateColorImage();
	}

	public Button getButton() {
		return this.fButton;
	}

	protected void updateColorImage() {
		Display display = this.fButton.getDisplay();

		GC gc = new GC(this.fImage);
		gc.setForeground(display.getSystemColor(2));
		gc.drawRectangle(0, 2, this.fExtent.x - 1, this.fExtent.y - 4);

		if (this.fColor != null) {
			this.fColor.dispose();
		}

		this.fColor = new Color(display, this.fColorValue);
		gc.setBackground(this.fColor);
		gc.fillRectangle(1, 3, this.fExtent.x - 2, this.fExtent.y - 5);
		gc.dispose();

		this.fButton.setImage(this.fImage);
	}

	protected Point computeImageSize(Control window) {
		GC gc = new GC(window);
		Font f = JFaceResources.getFontRegistry().get("org.eclipse.jface.defaultfont");
		gc.setFont(f);
		int height = gc.getFontMetrics().getHeight();
		gc.dispose();
		Point p = new Point(height * 3 - 6, height);
		return p;
	}

	private class ColourDisposeListener implements DisposeListener {
		public void widgetDisposed(DisposeEvent event) {
			if (ColorEditor.this.fImage != null) {
				ColorEditor.this.fImage.dispose();
				ColorEditor.this.fImage = null;
			}
			if (ColorEditor.this.fColor != null) {
				ColorEditor.this.fColor.dispose();
				ColorEditor.this.fColor = null;
			}
		}
	}

	private class ColourSelectionAdapter extends SelectionAdapter {
		public void widgetSelected(SelectionEvent event) {
			ColorDialog colorDialog = new ColorDialog(ColorEditor.this.fButton.getShell());
			colorDialog.setRGB(ColorEditor.this.fColorValue);
			RGB newColor = colorDialog.open();
			if (newColor != null) {
				ColorEditor.this.fColorValue = newColor;
				ColorEditor.this.updateColorImage();
			}
		}
	}
}