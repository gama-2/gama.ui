/*******************************************************************************************************
 *
 * RefreshActionProvider.java, in gama.ui.shared.navigator, is part of the source code of the GAMA modeling and
 * simulation platform (v.1.9.0).
 *
 * (c) 2007-2023 UMI 209 UMMISCO IRD/SU & Partners (IRIT, MIAT, TLU, CTU)
 *
 * Visit https://github.com/gama-platform/gama for license information and contacts.
 *
 ********************************************************************************************************/

package gama.ui.navigator.actions;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;

import gama.ui.shared.resources.GamaIcon;
import gama.ui.shared.resources.IGamaIcons;

/**
 * The Class RefreshActionProvider.
 */
public class RefreshActionProvider extends CommonActionProvider {

	/** The refresh action. */
	private RefreshAction refreshAction;

	/** The run all tests action. */
	private RunAllTestsAction runAllTestsAction;

	/** The shell. */
	private Shell shell;

	@Override
	public void init(final ICommonActionExtensionSite aSite) {
		super.init(aSite);
		shell = aSite.getViewSite().getShell();
		makeActions();
	}

	@Override
	public void fillActionBars(final IActionBars actionBars) {
		actionBars.setGlobalActionHandler(ActionFactory.REFRESH.getId(), refreshAction);
		updateActionBars();
	}

	@Override
	public void fillContextMenu(final IMenuManager menu) {
		final IStructuredSelection selection = (IStructuredSelection) getContext().getSelection();
		refreshAction.selectionChanged(selection);
		runAllTestsAction.selectionChanged(selection);
		if (runAllTestsAction.isEnabled()) { menu.appendToGroup("group.refresh", runAllTestsAction); }
		menu.appendToGroup("group.refresh", refreshAction);
	}

	/**
	 * Make actions.
	 */
	protected void makeActions() {
		final IShellProvider sp = () -> shell;
		refreshAction = new RefreshAction(sp);
		refreshAction.setImageDescriptor(GamaIcon.named(IGamaIcons.FILE_REFRESH).descriptor());
		refreshAction.setDisabledImageDescriptor(GamaIcon.named(IGamaIcons.FILE_REFRESH).disabledDescriptor());
		refreshAction.setActionDefinitionId(IWorkbenchCommandConstants.FILE_REFRESH);
		runAllTestsAction = new RunAllTestsAction(sp, "Run all tests");
		runAllTestsAction.setImageDescriptor(GamaIcon.named(IGamaIcons.TEST_RUN).descriptor());
		runAllTestsAction.setDisabledImageDescriptor(GamaIcon.named(IGamaIcons.TEST_RUN).disabledDescriptor());

	}

	@Override
	public void updateActionBars() {
		final IStructuredSelection selection = (IStructuredSelection) getContext().getSelection();
		refreshAction.selectionChanged(selection);
		runAllTestsAction.selectionChanged(selection);
	}

}
