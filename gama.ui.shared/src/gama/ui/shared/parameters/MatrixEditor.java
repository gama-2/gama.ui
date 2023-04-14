/*******************************************************************************************************
 *
 * MatrixEditor.java, in gama.ui.shared.shared, is part of the source code of the
 * GAMA modeling and simulation platform (v.1.9.0).
 *
 * (c) 2007-2022 UMI 209 UMMISCO IRD/SU & Partners (IRIT, MIAT, TLU, CTU)
 *
 * Visit https://github.com/gama-platform/gama for license information and contacts.
 * 
 ********************************************************************************************************/
package gama.ui.shared.parameters;

import org.eclipse.jface.dialogs.IDialogConstants;

import gama.core.kernel.experiment.IParameter;
import gama.core.metamodel.agent.IAgent;
import gama.core.runtime.IScope;
import gama.core.util.matrix.IMatrix;
import gama.ui.shared.interfaces.EditorListener;
import gama.ui.shared.utils.WorkbenchHelper;
import gaml.core.types.IType;
import gaml.core.types.Types;

/**
 * The Class MatrixEditor.
 */
public class MatrixEditor extends ExpressionBasedEditor<IMatrix<?>> {

	/**
	 * Instantiates a new matrix editor.
	 *
	 * @param scope the scope
	 * @param agent the agent
	 * @param param the param
	 * @param l the l
	 */
	MatrixEditor(final IScope scope, final IAgent agent, final IParameter param, final EditorListener<IMatrix<?>> l) {
		super(scope, agent, param, l);
	}

	@Override
	public void applyEdit() {

		final MatrixEditorDialog d = new MatrixEditorDialog(getScope(), WorkbenchHelper.getShell(), currentValue);
		if (d.open() == IDialogConstants.OK_ID) { modifyValue(d.getMatrix()); }

	}

	@Override
	protected void updateToolbar() {
		super.updateToolbar();
		editorToolbar.enable(EDIT, currentValue != null);
	}

	@SuppressWarnings ({ "unchecked", "rawtypes" })
	@Override
	public IType getExpectedType() {
		return Types.MATRIX;
	}

	@Override
	protected int[] getToolItems() {
		return new int[] { EDIT, REVERT };
	}

}
