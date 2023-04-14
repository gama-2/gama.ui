/*******************************************************************************************************
 *
 * WebHelperFactory.java, in gama.ui.shared.shared, is part of the source code of the
 * GAMA modeling and simulation platform (v.1.9.0).
 *
 * (c) 2007-2022 UMI 209 UMMISCO IRD/SU & Partners (IRIT, MIAT, TLU, CTU)
 *
 * Visit https://github.com/gama-platform/gama for license information and contacts.
 * 
 ********************************************************************************************************/
package gama.ui.shared.shared;

import org.eclipse.ui.services.AbstractServiceFactory;
import org.eclipse.ui.services.IServiceLocator;

import gama.ui.application.workbench.IWebHelper;
import gama.ui.shared.utils.WebHelper;

/**
 * A factory for creating WebHelper objects.
 */
public class WebHelperFactory extends AbstractServiceFactory {

	@Override
	public IWebHelper create(final Class serviceInterface, final IServiceLocator parentLocator,
			final IServiceLocator locator) {
		return WebHelper.getInstance();
	}

}
