/*******************************************************************************************************
 *
 * SwitchToSimulation.java, in gama.ui.shared.shared, is part of the source code of the
 * GAMA modeling and simulation platform (v.1.9.0).
 *
 * (c) 2007-2022 UMI 209 UMMISCO IRD/SU & Partners (IRIT, MIAT, TLU, CTU)
 *
 * Visit https://github.com/gama-platform/gama for license information and contacts.
 * 
 ********************************************************************************************************/
package gama.ui.shared.commands;

import gama.ui.application.workbench.PerspectiveHelper;

/**
 * The Class SwitchToSimulation.
 */
public class SwitchToSimulation extends SwitchToHandler {

	/**
	 * Execute.
	 */
	protected void execute() {
		PerspectiveHelper.switchToSimulationPerspective();
	}

}
