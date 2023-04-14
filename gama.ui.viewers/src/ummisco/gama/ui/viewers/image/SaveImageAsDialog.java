/*******************************************************************************************************
 *
 * SaveImageAsDialog.java, in gama.ui.shared.viewers, is part of the source code of the
 * GAMA modeling and simulation platform (v.1.9.0).
 *
 * (c) 2007-2022 UMI 209 UMMISCO IRD/SU & Partners (IRIT, MIAT, TLU, CTU)
 *
 * Visit https://github.com/gama-platform/gama for license information and contacts.
 * 
 ********************************************************************************************************/
package ummisco.gama.ui.viewers.image;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * Save As dialog that also includes a drop-down for the image type.
 */
class SaveImageAsDialog extends SaveAsDialog2 {

	/** The image types. */
	// These 3 arrays need to stay in sync
	static int[] IMAGE_TYPES =
			{ SWT.IMAGE_PNG, SWT.IMAGE_GIF, SWT.IMAGE_JPEG, SWT.IMAGE_BMP, SWT.IMAGE_ICO, SWT.IMAGE_TIFF };

	/** The image labels. */
	static String[] IMAGE_LABELS = { "PNG (Portable Network Graphics)", "GIF (Graphics Interchange Format)",
			"JPEG (Joint Photographic Experts Group)", "BMP (Bitmap)", "ICO (Icon File)",
			"TIFF (Tagged Image File Format)" };

	/** The image exts. */
	static String[] IMAGE_EXTS = { "png", //$NON-NLS-1$
			"gif", //$NON-NLS-1$
			"jpg", //$NON-NLS-1$
			"bmp", //$NON-NLS-1$
			"ico", //$NON-NLS-1$
			"tiff" //$NON-NLS-1$
	};

	/**
	 * The initial index of IMAGE_TYPES to select.
	 */
	private int initialImageTypeIndex = 0;

	/**
	 * The selected index of IMAGE_TYPES
	 */
	private int selectedImageTypeIndex = -1;

	/** The image type combo. */
	Combo imageTypeCombo;

	/**
	 * Instantiates a new save image as dialog.
	 *
	 * @param parentShell the parent shell
	 */
	public SaveImageAsDialog(final Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite parentComposite = (Composite) super.createDialogArea(parent);

		// create a composite with standard margins and spacing to put
		// out image specific stuff
		final Group composite = new Group(parentComposite, SWT.NONE);
		composite.setText("Image Information");
		final GridLayout layout = new GridLayout();
		// TODO: this isn't leaving any margin around the edge of the Group
		// in the dialog
		layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		layout.numColumns = 2;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		// composite.setFont(parentComposite.getFont());

		final Label l = new Label(composite, SWT.RIGHT);
		l.setText("Type:");
		GridData gd = new GridData();
		l.setLayoutData(gd);
		imageTypeCombo = new Combo(composite, SWT.BORDER | SWT.READ_ONLY | SWT.DROP_DOWN);
		gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = SWT.FILL;
		imageTypeCombo.setLayoutData(gd);

		// initialize the imageType drop-down
		// TODO: filter by what's actually going to work when saving
		for (final String element : IMAGE_LABELS) {
			imageTypeCombo.add(element);
		}
		if (initialImageTypeIndex >= 0 && initialImageTypeIndex < IMAGE_TYPES.length) {
			imageTypeCombo.select(initialImageTypeIndex);
			selectedImageTypeIndex = initialImageTypeIndex;
		} else {
			imageTypeCombo.select(0);
			selectedImageTypeIndex = 0;
		}
		// change the filename extension when the imageType is changed
		imageTypeCombo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				final int index = imageTypeCombo.getSelectionIndex();
				IPath path = Path.fromPortableString(getFileName());
				path = path.removeFileExtension().addFileExtension(IMAGE_EXTS[index]);
				setFileName(path.toPortableString());
			}
		});

		return parentComposite;
	}

	@Override
	protected void okPressed() {
		// save off the selected image type
		selectedImageTypeIndex = imageTypeCombo.getSelectionIndex();

		super.okPressed();
	}

	/**
	 * Set the initial filename and image type. This must be called before {@code}create(){@code}.
	 *
	 * @param basename
	 *            the file basename, no extension.
	 * @param type
	 *            the SWT.IMAGE_ type.
	 */
	public void setOriginalName(final String name, final int type) {
		StringBuilder basename = new StringBuilder().append(name);
		for (int i = 0; i < IMAGE_TYPES.length; i++) {
			if (type == IMAGE_TYPES[i]) {
				initialImageTypeIndex = i;
				basename.append(".").append(IMAGE_EXTS[i]); //$NON-NLS-1$
				break;
			}
		}
		setOriginalName(basename.toString());
	}

	/**
	 * Set the initial file and path and image type. This must be called before {@code}create(){@code}.
	 *
	 * @param origfile
	 *            the original file.
	 * @param type
	 *            the SWT.IMAGE_ type.
	 */
	public void setOriginalFile(final IFile file, final int type) {
		IFile origfile = file;
		for (int i = 0; i < IMAGE_TYPES.length; i++) {
			if (type == IMAGE_TYPES[i]) {
				initialImageTypeIndex = i;
				final String newname =
						origfile.getFullPath().removeFileExtension().addFileExtension(IMAGE_EXTS[i]).lastSegment();
				origfile = origfile.getParent().getFile(Path.fromPortableString(newname));
				break;
			}
		}
		setOriginalFile(origfile);
	}

	/**
	 * Get the selected image type.
	 */
	public int getSaveAsImageType() {
		if (selectedImageTypeIndex >= 0 && selectedImageTypeIndex < IMAGE_TYPES.length)
			return IMAGE_TYPES[selectedImageTypeIndex];
		else
			return IMAGE_TYPES[0];
	}

	/**
	 * Get the selected image type.
	 */
	public String getSaveAsImageExt() {
		if (selectedImageTypeIndex >= 0 && selectedImageTypeIndex < IMAGE_TYPES.length)
			return IMAGE_EXTS[selectedImageTypeIndex];
		else
			return IMAGE_EXTS[0];
	}
}