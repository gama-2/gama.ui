/*******************************************************************************************************
 *
 * MonitorDisplayer.java, in gama.ui.shared.shared, is part of the source code of the GAMA modeling and simulation
 * platform (v.1.9.0).
 *
 * (c) 2007-2022 UMI 209 UMMISCO IRD/SU & Partners (IRIT, MIAT, TLU, CTU)
 *
 * Visit https://github.com/gama-platform/gama for license information and contacts.
 *
 ********************************************************************************************************/
package gama.ui.shared.parameters;

import static gama.core.runtime.GAMA.getRuntimeScope;
import static gama.core.runtime.GAMA.reportError;
import static gama.core.util.GamaListFactory.wrap;
import static gama.ui.shared.menus.GamaMenu.action;
import static gama.ui.shared.menus.GamaMenu.separate;
import static gaml.core.operators.System.enterValue;
import static gaml.core.operators.System.userInputDialog;
import static gaml.core.types.Types.NO_TYPE;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;

import gama.core.common.interfaces.IValue;
import gama.core.kernel.experiment.InputParameter;
import gama.core.metamodel.agent.IAgent;
import gama.core.outputs.MonitorOutput;
import gama.core.outputs.ValuedDisplayOutputFactory;
import gama.core.runtime.IScope;
import gama.core.runtime.exceptions.GamaRuntimeException;
import gama.core.util.GamaColor;
import gama.ui.shared.controls.FlatButton;
import gama.ui.shared.resources.GamaColors;
import gama.ui.shared.resources.IGamaColors;
import gama.ui.shared.utils.WorkbenchHelper;
import gama.ui.shared.views.toolbar.Selector;
import gaml.core.expressions.IExpression;
import gaml.core.types.IType;
import gaml.core.types.Types;

/**
 * The Class TextDisplayer.
 */
public class MonitorDisplayer extends AbstractStatementEditor<MonitorOutput> {

	/** The closer. */
	private Runnable closer;

	/**
	 * Instantiates a new command editor.
	 *
	 * @param scope
	 *            the scope
	 * @param command
	 *            the command
	 * @param l
	 *            the l
	 */
	public MonitorDisplayer(final IScope scope, final MonitorOutput command) {
		super(scope, command, null);
	}

	@Override
	Composite createValueComposite() {
		composite = new Composite(parent, SWT.NONE);
		final var data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.minimumWidth = 100;
		data.horizontalSpan = 2;
		composite.setLayoutData(data);
		GridLayout l = new GridLayout(1, false);
		composite.setLayout(l);
		return composite;
	}

	@Override
	public void createControls(final EditorsGroup p) {
		super.createControls(p);
		if (getStatement().shouldBeInitialized()) {
			getStatement().shouldNotBeInitialized();
			applyEdit();
		}
	}

	@SuppressWarnings ("unchecked")
	@Override
	protected FlatButton createCustomParameterControl(final Composite composite) throws GamaRuntimeException {
		textBox = FlatButton.menu(composite, GamaColors.get(getStatement().getColor(getScope())),
				getStatement().getTitle());
		textBox.addSelectionListener((Selector) e -> {
			final Menu m = new Menu(textBox);
			action(m, "Edit...", ex -> { applyEdit(); });
			// item.setEnabled(false); // for the moment
			action(m, getStatement().isPaused() ? "Resume" : "Pause", ex -> {
				getStatement().setPaused(!getStatement().isPaused());
				updateWithValueOfParameter(false, false);
			});
			action(m, "Close", ex -> {
				closer.run();
				parent.requestLayout();
			});
			separate(m);
			action(m, "Copy value", ex -> {
				final Object v = getStatement().getLastValue();
				WorkbenchHelper
						.copy(v == null ? "nil" : v instanceof IValue ? ((IValue) v).serialize(true) : v.toString());
			});
			final IExpression exp = getStatement().getValue();
			final IType<?> type = exp == null ? Types.NO_TYPE : exp.getGamlType();
			if (type.isNumber() || type.isContainer() && type.getContentType().isNumber()) {
				action(m, "Save as CSV", ex -> { applySave(); });
			} else if (type.isAgentType()) {
				action(m, "Inspect agent", ex -> {
					getStatement().getScope().getGui().setSelectedAgent((IAgent) getStatement().getLastValue());
				});
			} else if (type.isContainer() && type.getContentType().isAgentType()) {
				action(m, "Browse agents", ex -> {
					ValuedDisplayOutputFactory.browse((Collection<? extends IAgent>) getStatement().getLastValue());
				});
			}
			m.setVisible(true);
		});
		composite.requestLayout();
		return textBox;

	}

	@Override
	EditorLabel createEditorLabel() {
		return null;
	}

	@Override
	public void updateWithValueOfParameter(final boolean synchronously, final boolean retrieveVarValue) {
		try {
			Runnable run = () -> {
				internalModification = true;
				if (!parent.isDisposed() && !textBox.isDisposed()) {
					textBox.setText(getStatement().getTitle());
					composite.update();
				}
				internalModification = false;
			};
			if (synchronously) {
				WorkbenchHelper.run(run);
			} else {
				WorkbenchHelper.asyncRun(run);
			}

		} catch (final GamaRuntimeException e) {
			e.addContext("Unable to obtain the value of " + name);
			reportError(getRuntimeScope(), e, false);
			return;
		}
	}

	@Override
	protected void applySave() {
		getStatement().saveHistory();
	}

	@Override
	protected void applyEdit() {
		GamaColor color = getStatement().getColor(getScope());
		if (color == null) { color = IGamaColors.NEUTRAL.gamaColor(); }
		Map<String, Object> init = userInputDialog(getScope(), "Edit monitor",
				wrap(NO_TYPE,
						List.of(enterValue(getScope(), "Title", getStatement().getName()),
								enterValue(getScope(), "Color", Types.COLOR, color),
								new InputParameter("Expression", getStatement().getValue()) {
									@Override
									public boolean isExpression() { return true; }
								})));
		getStatement().setName((String) init.get("Title"));
		getStatement().setColor((GamaColor) init.get("Color"));
		textBox.setColor(GamaColors.get(getStatement().getColor(getScope())));
		getStatement().setNewExpression((IExpression) init.get("Expression"));
		updateWithValueOfParameter(false, false);
	}

	@Override
	protected GridData getEditorControlGridData() {
		final var d = new GridData(SWT.FILL, SWT.CENTER, true, false);
		d.minimumWidth = 50;
		return d;
	}

	/**
	 * Sets the closer.
	 *
	 * @param object
	 *            the new closer
	 */
	public void setCloser(final Runnable object) { closer = object; }

}
