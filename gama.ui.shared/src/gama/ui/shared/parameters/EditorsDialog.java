/*******************************************************************************************************
 *
 * EditorsDialog.java, in gama.ui.shared.shared, is part of the source code of the GAMA modeling and simulation
 * platform (v.1.9.0).
 *
 * (c) 2007-2022 UMI 209 UMMISCO IRD/SU & Partners (IRIT, MIAT, TLU, CTU)
 *
 * Visit https://github.com/gama-platform/gama for license information and contacts.
 *
 ********************************************************************************************************/
package gama.ui.shared.parameters;

import static gama.ui.shared.resources.GamaColors.getTextColorForBackground;
import static gama.ui.shared.resources.GamaColors.setBackAndForeground;

import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import gama.core.kernel.experiment.IParameter;
import gama.core.runtime.IScope;
import gama.core.util.GamaColor;
import gama.core.util.GamaFont;
import gama.core.util.GamaMapFactory;
import gama.core.util.IMap;
import gama.ui.shared.interfaces.EditorListener;
import gama.ui.shared.resources.GamaColors;
import gama.ui.shared.utils.WorkbenchHelper;
import gaml.core.expressions.IExpression;

/**
 * The class EditorsDialog.
 *
 * @author drogoul
 * @since 10 mai 2012
 *
 */
public class EditorsDialog extends Dialog {

	/** The values. */
	private final IMap<String, Object> values = GamaMapFactory.createUnordered();

	/** The parameters. */
	private final List<IParameter> parameters;

	/** The title. */
	private final String title;

	/** The font. */
	private final GamaFont font;

	/** The scope. */
	private final IScope scope;

	/** The color. */
	private Color color;

	/**
	 * Instantiates a new editors dialog.
	 *
	 * @param scope
	 *            the scope.
	 * @param parentShell
	 *            the parent shell
	 * @param parameters
	 *            the parameters.
	 * @param title
	 *            the title.
	 * @param font
	 *            the font.
	 * @param color
	 *            the color
	 */
	public EditorsDialog(final IScope scope, final Shell parentShell, final List<IParameter> parameters,
			final String title, final GamaFont font, final GamaColor color, final Boolean showTitle) {
		super(parentShell);
		this.scope = scope;
		this.title = title;
		this.font = font;
		this.color = color == null ? null : GamaColors.toSwtColor(color);
		setShellStyle(showTitle ? SWT.TITLE | SWT.RESIZE | SWT.TOOL | SWT.ON_TOP : SWT.TOOL | SWT.ON_TOP);
		this.parameters = parameters;
		parameters.forEach(p -> { values.put(p.getName(), p.getInitialValue(scope)); });
	}

	@Override
	protected void createButtonsForButtonBar(final Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		final var above = (Composite) super.createDialogArea(parent);

		final var composite = new EditorsGroup(above);
		final var text = new Label(composite, SWT.None);
		text.setText(title);
		var data = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
		text.setLayoutData(data);
		if (font != null) {
			text.setFont(new Font(WorkbenchHelper.getDisplay(), font.getFontName(), font.getSize(), font.getStyle()));
		}
		final var sep = new Label(composite, SWT.NONE);
		data = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
		data.heightHint = 20;
		sep.setLayoutData(data);
		if (color == null) { color = above.getBackground(); }
		setBackAndForeground(color, getTextColorForBackground(this.color).color(), parent, above, composite, text, sep);
		parameters.forEach(param -> {
			final EditorListener<?> listener = newValue -> {
				param.setValue(scope, newValue);
				values.put(param.getName(), newValue);
			};
			if (param.isExpression()) {
				EditorFactory.createExpression(scope, composite, param.getName(), (IExpression) param.value(scope),
						listener, param.getType());
			} else {
				EditorFactory.create(scope, composite, param, listener, false, false);
			}
		});
		composite.layout();
		return composite;
	}

	@Override
	protected Control createButtonBar(final Composite parent) {
		Control composite = super.createButtonBar(parent);
		setBackAndForeground(color, getTextColorForBackground(this.color).color(), composite);
		return composite;
	}

	@Override
	protected boolean isResizable() { return true; }

	/**
	 * Gets the values.
	 *
	 * @return the values
	 */
	public Map<String, Object> getValues() { return values; }

}
