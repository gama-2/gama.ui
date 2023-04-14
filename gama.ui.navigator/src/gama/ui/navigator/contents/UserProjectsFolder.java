/*******************************************************************************************************
 *
 * UserProjectsFolder.java, in gama.ui.shared.navigator, is part of the source code of the GAMA modeling and simulation
 * platform (v.1.9.0).
 *
 * (c) 2007-2023 UMI 209 UMMISCO IRD/SU & Partners (IRIT, MIAT, TLU, CTU)
 *
 * Visit https://github.com/gama-platform/gama for license information and contacts.
 *
 ********************************************************************************************************/
package gama.ui.navigator.contents;

/**
 * The Class UserProjectsFolder.
 */
public class UserProjectsFolder extends TopLevelFolder {

	/**
	 * Instantiates a new user projects folder.
	 *
	 * @param root
	 *            the root
	 * @param name
	 *            the name
	 */
	public UserProjectsFolder(final NavigatorRoot root, final String name) {
		super(root, name, FOLDER_USER, "User-defined models", OK, null, Location.Other);
	}

}
