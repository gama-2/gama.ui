/*******************************************************************************************************
 *
 * IParameterEditor.java, in gama.ui.shared.shared, is part of the source code of the GAMA modeling and simulation
 * platform (v.1.9.0).
 *
 * (c) 2007-2022 UMI 209 UMMISCO IRD/SU & Partners (IRIT, MIAT, TLU, CTU)
 *
 * Visit https://github.com/gama-platform/gama for license information and contacts.
 *
 ********************************************************************************************************/
package gama.ui.shared.interfaces;

import gama.core.common.interfaces.IScoped;
import gama.core.kernel.experiment.IParameter;
import gama.ui.shared.parameters.EditorsGroup;
import gaml.core.types.IType;

/**
 * The class IParameterEditor.
 *
 * @author drogoul
 * @since 18 d�c. 2011
 *
 */
@SuppressWarnings ({ "rawtypes" })
public interface IParameterEditor<T> extends IScoped, Comparable<IParameterEditor<T>> {

	/**
	 * Gets the expected type.
	 *
	 * @return the expected type
	 */
	IType getExpectedType();

	/**
	 * Checks if is value modified.
	 *
	 * @return true, if is value modified
	 */
	boolean isValueModified();

	/**
	 * Revert to default value.
	 */
	void revertToDefaultValue();

	/**
	 * Gets the param.
	 *
	 * @return the param
	 */
	IParameter getParam();

	/**
	 * Update with value of parameter. The parameter, if true, forces the experiment to look for an updated value of the
	 * targeted variable
	 */
	void updateWithValueOfParameter(boolean synchronously, boolean retrieveVarValue);

	/**
	 * Sets the active.
	 *
	 * @param value
	 *            the new active
	 */
	void setActive(Boolean value);

	/**
	 * Gets the current value.
	 *
	 * @return the current value
	 */
	T getCurrentValue();

	/**
	 * Items to add to the editor
	 */

	int PLUS = 0;

	/** The minus. */
	int MINUS = 1;

	/** The edit. */
	int EDIT = 2;

	/** The inspect. */
	int INSPECT = 3;

	/** The browse. */
	int BROWSE = 4;

	/** The change. */
	int CHANGE = 5;

	/** The revert. */
	int REVERT = 6;

	/** The define. */
	int DEFINE = 7;

	/** The value. */
	int VALUE = 8;

	// int SAVE = 9;

	/**
	 * @param b
	 */
	void isSubParameter(boolean b);

	/**
	 * Gets the order.
	 *
	 * @return the order
	 */
	int getOrder();

	/**
	 * Creates the controls.
	 *
	 * @param compo the compo
	 */
	void createControls(EditorsGroup compo);

}