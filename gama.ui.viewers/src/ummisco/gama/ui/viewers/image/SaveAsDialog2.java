/*******************************************************************************************************
 *
 * SaveAsDialog2.java, in gama.ui.shared.viewers, is part of the source code of the
 * GAMA modeling and simulation platform (v.1.9.0).
 *
 * (c) 2007-2022 UMI 209 UMMISCO IRD/SU & Partners (IRIT, MIAT, TLU, CTU)
 *
 * Visit https://github.com/gama-platform/gama for license information and contacts.
 * 
 ********************************************************************************************************/
package ummisco.gama.ui.viewers.image;

import java.lang.reflect.Field;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.internal.ide.misc.ResourceAndContainerGroup;

/**
 * Subclass for the normal SaveAsDialog, to expose access to some of the UI.
 */
public class SaveAsDialog2 extends org.eclipse.ui.dialogs.SaveAsDialog {
	
	/** The resource group field. */
	private static Field resourceGroupField = null;

	/**
	 * Creates a new Save As dialog for no specific file.
	 *
	 * @param parentShell
	 *            the parent shell
	 */
	public SaveAsDialog2(final Shell parentShell) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.SHEET);
	}

	/**
	 * Get the "resourceGroup" field for the specified dialog. This is a silly hack to workaround
	 * org.eclipse.ui.dialogs.SaveAsDialog's resourceGroup field and class not being public or protected, or otherwise
	 * supporting methods to manipulate the UI programatically.
	 */
	protected static synchronized ResourceAndContainerGroup getResourceGroup(final SaveAsDialog2 d) {
		boolean origAccessible = false;
		try {
			// cache it to avoid unneccessary reflection look ups
			if (resourceGroupField == null) {
				resourceGroupField = org.eclipse.ui.dialogs.SaveAsDialog.class.getDeclaredField("resourceGroup"); //$NON-NLS-1$
			}

			// make it accessible, since it's normally private
			origAccessible = resourceGroupField.isAccessible();
			try {
				resourceGroupField.setAccessible(true);
				return (ResourceAndContainerGroup) resourceGroupField.get(d);
			} finally {
				resourceGroupField.setAccessible(origAccessible);
			}
		} catch (final Exception ex) {
			return null;
		}
	}

	/**
	 * Returns the current file name as entered by the user, or its anticipated initial value.
	 *
	 * @return the file name, its anticipated initial value, or <code>null</code> if no file name is known
	 */
	public String getFileName() {
		final ResourceAndContainerGroup resourceGroup = getResourceGroup(this);
		if (resourceGroup != null) { return resourceGroup.getResource(); }

		return ""; //$NON-NLS-1$
	}

	/**
	 * Set the filename of the dialog.
	 */
	public void setFileName(final String filename) {
		final ResourceAndContainerGroup resourceGroup = getResourceGroup(this);
		if (resourceGroup != null) {
			resourceGroup.setResource(filename);
		} else {
			setOriginalName(filename);
		}
	}
}