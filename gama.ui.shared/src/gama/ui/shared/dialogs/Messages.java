/*******************************************************************************************************
 *
 * Messages.java, in gama.ui.shared.shared, is part of the source code of the
 * GAMA modeling and simulation platform (v.1.9.0).
 *
 * (c) 2007-2022 UMI 209 UMMISCO IRD/SU & Partners (IRIT, MIAT, TLU, CTU)
 *
 * Visit https://github.com/gama-platform/gama for license information and contacts.
 * 
 ********************************************************************************************************/
package gama.ui.shared.dialogs;

import org.eclipse.jface.dialogs.MessageDialog;

import gama.ui.shared.utils.WorkbenchHelper;

/**
 * The Class Messages.
 */
public class Messages {

	/**
	 * Error.
	 *
	 * @param error
	 *            the error
	 */
	public static void error(final String error) {
		WorkbenchHelper.run(() -> MessageDialog.openError(null, "Error", error));
	}

	/**
	 * Tell.
	 *
	 * @param error
	 *            the error
	 */
	public static void tell(final String error) {
		WorkbenchHelper.run(() -> MessageDialog.openInformation(null, "Message", error));
	}

	/**
	 * Question.
	 *
	 * @param title
	 *            the title
	 * @param message
	 *            the message
	 * @return true, if successful
	 */
	public static boolean question(final String title, final String message) {
		return WorkbenchHelper.run(() -> MessageDialog.openQuestion(null, title, message));
	}

	/**
	 * Confirm.
	 *
	 * @param title
	 *            the title
	 * @param message
	 *            the message
	 * @return true, if successful
	 */
	public static boolean confirm(final String title, final String message) {
		return WorkbenchHelper.run(() -> MessageDialog.openConfirm(null, title, message));
	}

}
