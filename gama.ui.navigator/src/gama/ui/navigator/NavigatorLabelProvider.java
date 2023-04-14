/*******************************************************************************************************
 *
 * NavigatorLabelProvider.java, in gama.ui.shared.navigator, is part of the source code of the GAMA modeling and
 * simulation platform (v.1.9.0).
 *
 * (c) 2007-2022 UMI 209 UMMISCO IRD/SU & Partners (IRIT, MIAT, TLU, CTU)
 *
 * Visit https://github.com/gama-platform/gama for license information and contacts.
 *
 ********************************************************************************************************/
package gama.ui.navigator;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import gama.ui.navigator.contents.VirtualContent;

/**
 * The Class NavigatorLabelProvider.
 */
public class NavigatorLabelProvider extends CellLabelProvider
		implements ILabelProvider, IColorProvider /* IFontProvider */ {

	@Override
	public String getText(final Object element) {
		if (element instanceof VirtualContent) return ((VirtualContent<?>) element).getName();
		return null;
	}

	@Override
	public Image getImage(final Object element) {
		if (element instanceof VirtualContent vc) {
			ImageDescriptor id = vc.getImageDescriptor();
			if (id != null) return id.createImage();
		}
		return null;
	}

	@Override
	public boolean isLabelProperty(final Object element, final String property) {
		return false;
	}

	// @Override
	// public Font getFont(final Object element) {
	// if (element instanceof VirtualContent) { return ((VirtualContent<?>) element).getFont(); }
	// return GamaFonts.getNavigFolderFont();
	// }

	@Override
	public Color getForeground(final Object element) {
		if (element instanceof VirtualContent) return ((VirtualContent<?>) element).getColor();
		return null;
	}

	@Override
	public Color getBackground(final Object element) {
		return null;
	}

	@Override
	public void update(final ViewerCell cell) {}

}
