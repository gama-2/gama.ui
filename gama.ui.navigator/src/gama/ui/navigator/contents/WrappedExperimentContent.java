/*******************************************************************************************************
 *
 * WrappedExperimentContent.java, in gama.ui.shared.navigator, is part of the source code of the GAMA modeling and
 * simulation platform (v.1.9.0).
 *
 * (c) 2007-2022 UMI 209 UMMISCO IRD/SU & Partners (IRIT, MIAT, TLU, CTU)
 *
 * Visit https://github.com/gama-platform/gama for license information and contacts.
 *
 ********************************************************************************************************/
package gama.ui.navigator.contents;

import gama.core.common.interfaces.IGamlLabelProvider;
import gama.core.runtime.GAMA;
import gama.ui.shared.utils.WorkbenchHelper;
import gaml.core.compilation.ast.ISyntacticElement;

/**
 * The Class WrappedExperimentContent.
 */
public class WrappedExperimentContent extends WrappedSyntacticContent {

	/**
	 * Instantiates a new wrapped experiment content.
	 *
	 * @param file
	 *            the file
	 * @param e
	 *            the e
	 */
	public WrappedExperimentContent(final WrappedGamaFile file, final ISyntacticElement e) {
		super(file, e, WorkbenchHelper.getService(IGamlLabelProvider.class).getText(e));
	}

	@Override
	public WrappedGamaFile getFile() { return (WrappedGamaFile) getParent(); }

	@Override
	public boolean hasChildren() {
		return true;
	}

	@Override
	public boolean handleDoubleClick() {
		GAMA.getGui().runModel(getParent(), element.getName());
		return true;
	}
}