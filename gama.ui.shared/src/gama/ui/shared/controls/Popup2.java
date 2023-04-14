/*******************************************************************************************************
 *
 * Popup2.java, in gama.ui.shared.shared, is part of the source code of the
 * GAMA modeling and simulation platform (v.1.9.0).
 *
 * (c) 2007-2022 UMI 209 UMMISCO IRD/SU & Partners (IRIT, MIAT, TLU, CTU)
 *
 * Visit https://github.com/gama-platform/gama for license information and contacts.
 * 
 ********************************************************************************************************/
package gama.ui.shared.controls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.dialogs.PopupDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TypedListener;
import org.eclipse.swt.widgets.Widget;

import gama.ui.shared.controls.IPopupProvider.PopupText;
import gama.ui.shared.resources.GamaColors;
import gama.ui.shared.utils.WorkbenchHelper;
import gaml.core.compilation.GamlIdiomsProvider;

/**
 * The class Popup2. An alternative to Popup, which uses JFace dialogs
 *
 * @author drogoul
 * @since 31 oct. 2017
 *
 */
public class Popup2 extends PopupDialog {

	/** The contents. */
	Composite parent, contents;

	/** The hide. */
	final Listener hide = event -> hide();
	
	/** The display. */
	final Runnable display = () -> WorkbenchHelper.asyncRun(this::display);

	/** The mtl. */
	private final MouseTrackListener mtl = new MouseTrackListener() {

		@Override
		public void mouseEnter(final MouseEvent e) {
			display.run();
		}

		@Override
		public void mouseExit(final MouseEvent e) {
			hide();
		}

		@Override
		public void mouseHover(final MouseEvent e) {
			display.run();

		}

	};

	/** The provider. */
	private final IPopupProvider provider;

	/**
	 * Instantiates a new popup 2.
	 *
	 * @param provider the provider
	 * @param controls the controls
	 */
	/*
	 *
	 */
	public Popup2(final IPopupProvider provider, final Widget... controls) {
		super(WorkbenchHelper.getShell(), PopupDialog.HOVER_SHELLSTYLE, false, false, false, false, false, null, null);
		this.provider = provider;
		final Shell parent = provider.getControllingShell();
		parent.addListener(SWT.Move, hide);
		parent.addListener(SWT.Resize, hide);
		parent.addListener(SWT.Close, hide);
		parent.addListener(SWT.Deactivate, hide);
		parent.addListener(SWT.Hide, hide);
		parent.addListener(SWT.Dispose, event -> close());
		for (final Widget c : controls) {
			if (c == null) { continue; }
			final TypedListener typedListener = new TypedListener(mtl);
			c.addListener(SWT.MouseEnter, typedListener);
			c.addListener(SWT.MouseExit, typedListener);
			c.addListener(SWT.MouseHover, typedListener);
		}
	}

	@Override
	protected Control createContents(final Composite parent) {
		this.parent = parent;
		if (contents == null) { this.contents = (Composite) super.createDialogArea(parent); }
		// We then grab the text and hide if it is null or empty
		final PopupText s = provider.getPopupText();
		if (s == null || s.isEmpty()) {
			hide();
			return null;
		}
		final Control[] array = contents.getChildren();
		final List<Control> labels = new ArrayList<>(Arrays.asList(array));
		final int labelsSize = s.size();

		final int controlsSize = array.length;
		if (controlsSize > labelsSize) {
			for (int i = labelsSize; i < controlsSize; i++) { labels.get(i).dispose(); }
		} else if (labelsSize > controlsSize) {
			for (int i = 0; i < labelsSize - controlsSize; i++) {
				final Label label = new Label(contents, SWT.NONE);
				label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
				labels.add(label);
			}
		}

		final Iterator<Control> it = labels.iterator();
		s.forEach((text, color) -> {
			final Label label = (Label) it.next();
			label.setBackground(color.color());
			label.setForeground(GamaColors.getTextColorForBackground(color.color()).color());
			label.setText(GamlIdiomsProvider.toText(text));
		});
		return contents;
	}

	/**
	 * Update contents.
	 */
	public void updateContents() {
		createContents(parent);
	}

	@Override
	protected boolean hasTitleArea() {
		return false;
	}

	@Override
	protected boolean hasInfoArea() {
		return false;
	}

	@Override
	protected void showDialogMenu() {}

	@Override
	protected void setInfoText(final String text) {}

	@Override
	protected void setTitleText(final String text) {}

	@Override
	protected boolean getPersistLocation() { return false; }

	@Override
	protected boolean getPersistSize() { return false; }

	@Override
	protected void saveDialogBounds(final Shell shell) {}

	@Override
	protected Point getDefaultSize() {
		int width = provider.getPopupWidth();
		if (width <= 0) { width = SWT.DEFAULT; }
		return getShell().computeSize(width, SWT.DEFAULT, true);

	}

	@Override
	protected Point getDefaultLocation(final Point initialSize) {
		return provider.getAbsoluteOrigin();
	}

	/**
	 * Checks if is visible.
	 *
	 * @return true, if is visible
	 */
	public boolean isVisible() { return getShell() != null && getShell().isVisible(); }

	/**
	 * Adjust size.
	 */
	protected void adjustSize() {
		final Shell shell = getShell();
		shell.layout();
		shell.pack();
		shell.setLocation(getDefaultLocation(null));
		shell.setSize(getDefaultSize());
	}

	/**
	 * Display.
	 */
	public void display() {
		if (getShell() != null && !getShell().isDisposed()) {
			updateContents();
			adjustSize();
			getShell().setVisible(true);
		} else {
			open();
		}
	}

	/**
	 * Hide.
	 */
	public void hide() {
		if (getShell() != null && !getShell().isDisposed()) { getShell().setVisible(false); }
	}
}
