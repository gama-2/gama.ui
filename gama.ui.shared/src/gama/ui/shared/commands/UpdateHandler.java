/*******************************************************************************************************
 *
 * UpdateHandler.java, in gama.ui.shared.shared, is part of the source code of the GAMA modeling and simulation
 * platform (v.1.9.0).
 *
 * (c) 2007-2023 UMI 209 UMMISCO IRD/SU & Partners (IRIT, MIAT, TLU, CTU)
 *
 * Visit https://github.com/gama-platform/gama for license information and contacts.
 *
 ********************************************************************************************************/
package gama.ui.shared.commands;

import static gama.ui.shared.utils.WorkbenchHelper.getCommand;
import static gama.ui.shared.utils.WorkbenchHelper.runCommand;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.internal.AbstractEnabledHandler;

import gama.ui.application.workspace.WorkspacePreferences;

/**
 * The Class UpdateHandler.
 */
public class UpdateHandler extends AbstractEnabledHandler implements IHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		runCommand(getCommand("org.eclipse.equinox.p2.ui.sdk.update"), event);
		WorkspacePreferences.forceWorkspaceRebuild();
		// GAMA.getGui().refreshNavigator();
		return this;
	}

}
