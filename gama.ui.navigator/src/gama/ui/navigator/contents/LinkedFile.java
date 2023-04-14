/*******************************************************************************************************
 *
 * LinkedFile.java, in gama.ui.shared.navigator, is part of the source code of the GAMA modeling and simulation
 * platform (v.1.9.0).
 *
 * (c) 2007-2023 UMI 209 UMMISCO IRD/SU & Partners (IRIT, MIAT, TLU, CTU)
 *
 * Visit https://github.com/gama-platform/gama for license information and contacts.
 *
 ********************************************************************************************************/
package gama.ui.navigator.contents;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;

import gama.ui.shared.utils.WorkbenchHelper;

/**
 * Class LinkedFile.
 *
 * @author drogoul
 * @since 5 févr. 2015
 *
 */
public class LinkedFile extends VirtualContent<Category> implements IAdaptable {

	/** The file. */
	final WrappedFile file;

	/** The suffix. */
	final String suffix;

	/**
	 * @param root
	 * @param name
	 */
	public LinkedFile(final Category root, final IFile wrapped, final String originalName) {
		super(root, NavigatorRoot.getInstance().getManager().findWrappedInstanceOf(wrapped).getName());
		suffix = originalName;
		file = (WrappedFile) getManager().findWrappedInstanceOf(wrapped);
	}

	/**
	 * Method hasChildren()
	 *
	 * @see gama.ui.shared.navigator.contents.VirtualContent#hasChildren()
	 */
	@Override
	public boolean hasChildren() {
		return false;
	}

	/**
	 * Method getNavigatorChildren()
	 *
	 * @see gama.ui.shared.navigator.contents.VirtualContent#getNavigatorChildren()
	 */
	@Override
	public Object[] getNavigatorChildren() { return EMPTY; }

	/**
	 * Method getImage()
	 *
	 * @see gama.ui.shared.navigator.contents.VirtualContent#getImage()
	 */
	@Override
	public ImageDescriptor getImageDescriptor() { return  ImageDescriptor.createFromImage(DEFAULT_LABEL_PROVIDER.getImage(file.getResource())); }

	@Override
	public boolean handleDoubleClick() {
		try {
			IDE.openEditor(WorkbenchHelper.getPage(), file.getResource());
		} catch (final PartInitException e1) {
			e1.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Method getAdapter()
	 *
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	@SuppressWarnings ({ "unchecked", "rawtypes" })
	@Override
	public Object getAdapter(final Class adapter) {
		return adapter == IResource.class || adapter == IFile.class ? file.getResource() : null;
	}

	@Override
	public int findMaxProblemSeverity() {
		return file.findMaxProblemSeverity();
	}

	@Override
	public void getSuffix(final StringBuilder sb) {
		sb.append(suffix);
	}

	@Override
	public ImageDescriptor getOverlay() { return null; }

	@Override
	public VirtualContentType getType() { return VirtualContentType.FILE_REFERENCE; }

	/**
	 * Gets the target.
	 *
	 * @return the target
	 */
	public WrappedFile getTarget() { return file; }
}
