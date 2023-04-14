/*******************************************************************************************************
 *
 * PluginsModelsFolder.java, in gama.ui.shared.navigator, is part of the source code of the GAMA modeling and
 * simulation platform (v.1.9.0).
 *
 * (c) 2007-2023 UMI 209 UMMISCO IRD/SU & Partners (IRIT, MIAT, TLU, CTU)
 *
 * Visit https://github.com/gama-platform/gama for license information and contacts.
 *
 ********************************************************************************************************/
package gama.ui.navigator.contents;

import gama.ui.shared.utils.WorkbenchHelper;

/**
 * The Class PluginsModelsFolder.
 */
public class PluginsModelsFolder extends TopLevelFolder {

	/**
	 * Instantiates a new plugins models folder.
	 *
	 * @param root
	 *            the root
	 * @param name
	 *            the name
	 */
	public PluginsModelsFolder(final NavigatorRoot root, final String name) {
		super(root, name, FOLDER_PLUGIN, "Models present in GAMA plugins", WARNING, WorkbenchHelper.PLUGIN_NATURE,
				Location.Plugins);
	}

}
