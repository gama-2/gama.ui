/*******************************************************************************************************
 *
 * PreferencesHelper.java, in gama.ui.shared.shared, is part of the source code of the
 * GAMA modeling and simulation platform (v.1.9.0).
 *
 * (c) 2007-2022 UMI 209 UMMISCO IRD/SU & Partners (IRIT, MIAT, TLU, CTU)
 *
 * Visit https://github.com/gama-platform/gama for license information and contacts.
 * 
 ********************************************************************************************************/
package gama.ui.shared.utils;

import static gama.core.common.interfaces.IGui.NAVIGATOR_LIGHTWEIGHT_DECORATOR_ID;
import static gama.core.common.preferences.GamaPreferences.create;
import static gama.core.common.preferences.GamaPreferences.Interface.APPEARANCE;
import static gama.core.common.preferences.GamaPreferences.Interface.MENUS;
import static gama.core.common.preferences.GamaPreferences.Interface.NAME;
import static gama.core.util.GamaColor.getNamed;
import static gama.ui.shared.menus.GamaColorMenu.SORT_NAMES;
import static gama.ui.shared.menus.GamaColorMenu.byBrightness;
import static gama.ui.shared.menus.GamaColorMenu.byLuminescence;
import static gama.ui.shared.menus.GamaColorMenu.byName;
import static gama.ui.shared.menus.GamaColorMenu.byRGB;
import static gama.ui.shared.menus.GamaColorMenu.colorComp;
import static gama.ui.shared.resources.GamaColors.toGamaColor;
import static gama.ui.shared.resources.IGamaColors.WARNING;
import static org.eclipse.ui.PlatformUI.getWorkbench;

import org.eclipse.core.runtime.CoreException;

import gama.core.common.preferences.GamaPreferences;
import gama.core.common.preferences.Pref;
import gama.core.runtime.MemoryUtils;
import gama.core.util.GamaColor;
import gama.ui.shared.menus.GamaColorMenu;
import gama.ui.shared.resources.IGamaColors;
import gama.ui.shared.views.GamaPreferencesView;
import gaml.core.types.IType;

/**
 * The Class PreferencesHelper.
 */
public class PreferencesHelper {

	/** The Constant CORE_EDITORS_HIGHLIGHT. */
	public static final Pref<Boolean> CORE_EDITORS_HIGHLIGHT =
			create("pref_editors_highligth", "Highlight in yellow the title of value editors when they change", true,
					IType.BOOL, true).in(NAME, APPEARANCE);

	/** The Constant SHAPEFILE_VIEWER_FILL. */
	public static final Pref<GamaColor> SHAPEFILE_VIEWER_FILL = create("pref_shapefile_background_color",
			"Shapefile viewer fill color", () -> getNamed("lightgray"), IType.COLOR, false).in(NAME, APPEARANCE);

	/** The Constant SHAPEFILE_VIEWER_LINE_COLOR. */
	public static final Pref<GamaColor> SHAPEFILE_VIEWER_LINE_COLOR = create("pref_shapefile_line_color",
			"Shapefile viewer line color", () -> getNamed("black"), IType.COLOR, false).in(NAME, APPEARANCE);

	/** The Constant ERROR_TEXT_COLOR. */
	public static final Pref<GamaColor> ERROR_TEXT_COLOR =
			create("pref_error_text_color", "Text color of errors", () -> toGamaColor(IGamaColors.ERROR.inactive()),
					IType.COLOR, true).in(GamaPreferences.Runtime.NAME, GamaPreferences.Runtime.ERRORS);

	/** The Constant WARNING_TEXT_COLOR. */
	public static final Pref<GamaColor> WARNING_TEXT_COLOR =
			create("pref_warning_text_color", "Text color of warnings", () -> toGamaColor(WARNING.inactive()),
					IType.COLOR, true).in(GamaPreferences.Runtime.NAME, GamaPreferences.Runtime.ERRORS);

	/** The Constant IMAGE_VIEWER_BACKGROUND. */
	public static final Pref<GamaColor> IMAGE_VIEWER_BACKGROUND =
			create("pref_image_background_color", "Image viewer background color", () -> GamaColor.getNamed("white"),
					IType.COLOR, false).in(NAME, APPEARANCE);

	// public static final Pref<GamaFont> BASE_BUTTON_FONT = create("pref_button_font", "Font of buttons and dialogs",
	// () -> new GamaFont(getBaseFont(), SWT.BOLD, baseSize), IType.FONT, false).in(NAME, APPEARANCE)
	// .onChange(GamaFonts::setLabelFont);

	/** The color menu sort. */
	public static Pref<String> COLOR_MENU_SORT =
			create("pref_menu_colors_sort", "Sort colors menu by", "RGB value", IType.STRING, false).among(SORT_NAMES)
					.activates("menu.colors.reverse", "menu.colors.group").in(NAME, MENUS).onChange(pref -> {
						if (pref.equals(SORT_NAMES[0])) {
							colorComp = byRGB;
						} else if (pref.equals(SORT_NAMES[1])) {
							colorComp = byName;
						} else if (pref.equals(SORT_NAMES[2])) {
							colorComp = byBrightness;
						} else {
							colorComp = byLuminescence;
						}
					});

	/** The color menu reverse. */
	public static Pref<Boolean> COLOR_MENU_REVERSE =
			create("pref_menu_colors_reverse", "Reverse order", false, IType.BOOL, false).in(NAME, MENUS)
					.onChange(pref -> GamaColorMenu.setReverse(pref ? -1 : 1));

	/** The color menu group. */
	public static Pref<Boolean> COLOR_MENU_GROUP =
			create("pref_menu_colors_group", "Group colors", false, IType.BOOL, false).in(NAME, MENUS)
					.onChange(pref -> GamaColorMenu.breakdown = pref);

	/** The Constant NAVIGATOR_METADATA. */
	public static final Pref<Boolean> NAVIGATOR_METADATA =
			create("pref_navigator_display_metadata", "Display metadata in navigator", true, IType.BOOL, false)
					.in(NAME, APPEARANCE).onChange(newValue -> {
						final var mgr = getWorkbench().getDecoratorManager();
						try {
							mgr.setEnabled(NAVIGATOR_LIGHTWEIGHT_DECORATOR_ID, newValue);
						} catch (final CoreException e) {
							e.printStackTrace();
						}

					});

	/**
	 * Initialize.
	 */
	public static void initialize() {
		final var ini = MemoryUtils.findIniFile();
		Integer writtenMemory = ini == null ? null : MemoryUtils.readMaxMemoryInMegabytes(ini);
		final var text = ini == null || writtenMemory == null
				? "The max. memory allocated in Megabytes. It can be modified in Eclipse (developer version) or in Gama.ini file"
				: "Maximum memory allocated in Mb (requires to restart GAMA)";
		final var maxMemory = writtenMemory == null ? MemoryUtils.maxMemory() : writtenMemory;
		final var p = GamaPreferences.create("pref_memory_max", text, maxMemory, 1, false)
				.in(GamaPreferences.Runtime.NAME, GamaPreferences.Runtime.MEMORY);
		// Force the value to the one contained in the ini file or in the arguments.
		// Trick to force the pref to be read
		p.getValue();
		p.set(maxMemory);
		if (writtenMemory == null) { p.disabled(); }
		p.onChange(newValue -> {
			MemoryUtils.changeMaxMemory(ini, newValue);
			GamaPreferencesView.setRestartRequired();
		});

	}

}
