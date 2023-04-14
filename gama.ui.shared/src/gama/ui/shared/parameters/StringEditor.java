/*******************************************************************************************************
 *
 * StringEditor.java, in gama.ui.shared.shared, is part of the source code of the GAMA modeling and simulation platform
 * (v.1.9.0).
 *
 * (c) 2007-2022 UMI 209 UMMISCO IRD/SU & Partners (IRIT, MIAT, TLU, CTU)
 *
 * Visit https://github.com/gama-platform/gama for license information and contacts.
 *
 ********************************************************************************************************/
package gama.ui.shared.parameters;

import gama.core.kernel.experiment.IParameter;
import gama.core.metamodel.agent.IAgent;
import gama.core.runtime.IScope;
import gama.ui.shared.interfaces.EditorListener;
import gaml.core.types.IType;
import gaml.core.types.Types;

/**
 * The Class StringEditor.
 */
public class StringEditor extends ExpressionBasedEditor<String> {

	/**
	 * Instantiates a new string editor.
	 *
	 * @param scope
	 *            the scope
	 * @param agent
	 *            the agent
	 * @param param
	 *            the param
	 * @param l
	 *            the l
	 */
	StringEditor(final IScope scope, final IAgent agent, final IParameter param, final EditorListener<String> l) {
		super(scope, agent, param, l);
	}

	@Override
	public IType<String> getExpectedType() { return Types.STRING; }

}
