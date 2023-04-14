/*******************************************************************************************************
 *
 * MapLayerComposite.java, in gama.ui.shared.viewers, is part of the source code of the GAMA modeling and simulation
 * platform (v.1.9.0).
 *
 * (c) 2007-2023 UMI 209 UMMISCO IRD/SU & Partners (IRIT, MIAT, TLU, CTU)
 *
 * Visit https://github.com/gama-platform/gama for license information and contacts.
 *
 ********************************************************************************************************/

package gama.ui.viewers.gis;

import java.util.Collections;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;

import gama.ui.shared.resources.GamaIcon;
import gama.ui.shared.resources.IGamaIcons;

/**
 * Displays a list of the map layers in an associated and provides controls to set the visibility, selection and style
 * of each layer.
 * <p>
 * Implementation note: DefaultMapContext stores its list of MapLayer objects in rendering order, ie. the layer at index
 * 0 is rendererd first, followed by index 1 etc. MapLayerTable stores its layers in the reverse order since it is more
 * intuitive for the user to think of a layer being 'on top' of other layers.
 *
 * @author Andrea Antonello (www.hydrologis.com)
 * @author Michael Bedward
 *
 *
 *
 * @source $URL$
 */
public class MapLayerComposite extends Composite {

	/** The pane. */
	protected SwtMapPane pane;

	/** The map layer table viewer. */
	protected MaplayerTableViewer mapLayerTableViewer;

	/**
	 */
	public MapLayerComposite(final Composite parent, final int style) {
		super(parent, style);
		init();
	}

	/**
	 * Set the map pane that the MapLayerTable will service.
	 *
	 * @param pane
	 *            the map pane
	 */
	public void setMapPane(final SwtMapPane pane) {
		this.pane = pane;

		mapLayerTableViewer.clear();

		pane.setMapLayerTable(this);
		mapLayerTableViewer.setPane(pane);

		final MapContent mapContent = pane.getMapContent();
		final List<Layer> layers = mapContent.layers();
		for (final Layer mapLayer : layers) { mapLayerTableViewer.addLayer(mapLayer); }
	}

	/**
	 * Add a new layer to those listed in the table. This method will be called by the associated map pane automatically
	 * as part of the event sequence when a new MapLayer is added to the pane's MapContext.
	 *
	 * @param layer
	 *            the map layer
	 */
	public void onAddLayer(final Layer layer) {
		mapLayerTableViewer.addLayer(layer);
	}

	/**
	 * Remove a layer from those listed in the table. This method will be called by the associated map pane
	 * automatically as part of the event sequence when a new MapLayer is removed from the pane's MapContext.
	 *
	 * @param layer
	 *            the map layer
	 */
	public void onRemoveLayer(final Layer layer) {
		mapLayerTableViewer.removeLayer(layer);
	}

	/**
	 * Repaint the list item associated with the specified MapLayer object
	 *
	 * @param layer
	 *            the map layer
	 */
	public void repaint(final Layer layer) {
		mapLayerTableViewer.refresh(layer, true);
	}

	/**
	 * Called by the constructor. This method lays out the components that make up the MapLayerTable and registers a
	 * mouse listener.
	 */
	private void init() {
		setLayout(new GridLayout(1, false));

		final Group mapLayersGroup = new Group(this, SWT.NONE);
		mapLayersGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		mapLayersGroup.setLayout(new GridLayout(1, false));
		mapLayersGroup.setText("Layers");

		mapLayerTableViewer = new MaplayerTableViewer(mapLayersGroup, SWT.BORDER | SWT.FULL_SELECTION);
		final GridData listGD = new GridData(SWT.FILL, SWT.FILL, true, true);
		mapLayerTableViewer.getTable().setLayoutData(listGD);

		final Composite buttonComposite = new Composite(mapLayersGroup, SWT.NONE);
		buttonComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		buttonComposite.setLayout(new GridLayout(5, true));

		final Button showLayersButton = new Button(buttonComposite, SWT.PUSH);
		showLayersButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		showLayersButton.setToolTipText("Show all layers");
		showLayersButton.setImage(GamaIcon.named(IGamaIcons.SMALL_INSPECT).image());
		showLayersButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				onShowAllLayers();
			}
		});

		final Button hideLayersButton = new Button(buttonComposite, SWT.PUSH);
		hideLayersButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		hideLayersButton.setToolTipText("Hide all layers");
		hideLayersButton.setImage(GamaIcon.named(IGamaIcons.SMALL_HIDDEN).image());
		hideLayersButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				onHideAllLayers();
			}
		});

		final Button layerUpButton = new Button(buttonComposite, SWT.PUSH);
		layerUpButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		layerUpButton.setToolTipText("Layer up");
		layerUpButton.setImage(GamaIcon.named(IGamaIcons.SMALL_COLLAPSE).image());
		layerUpButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				moveLayer(-1);
			}
		});

		final Button layerDownButton = new Button(buttonComposite, SWT.PUSH);
		layerDownButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		layerDownButton.setToolTipText("Layer down");
		layerDownButton.setImage(GamaIcon.named(IGamaIcons.SMALL_EXPAND).image());
		layerDownButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				moveLayer(1);
			}
		});
	}

	/**
	 * Handle a ListDataEvent signallying a drag-reordering of the map layers. The event is published by the list model
	 * after the layers have been reordered there.
	 *
	 * @param ev
	 *            the event
	 */
	void moveLayer(final int delta) {
		final Layer selectedMapLayer = mapLayerTableViewer.getSelectedMapLayer();
		if (selectedMapLayer == null) return;
		final List<Layer> layersList = mapLayerTableViewer.getLayersList();
		final MapContent mapContent = pane.getMapContent();

		final int contextIndex = mapContent.layers().indexOf(selectedMapLayer);

		final int viewerIndex = layersList.indexOf(selectedMapLayer);
		final int newViewerIndex = viewerIndex + delta;
		if (newViewerIndex < 0 || newViewerIndex > layersList.size() - 1) return;

		/*
		 * MapLayerTable stores layers in the reverse order to DefaultMapContext (see comment in javadocs for this
		 * class)
		 */
		final int newContextIndex = contextIndex - delta;
		if (newContextIndex < 0 || newContextIndex > mapContent.layers().size() - 1) return;

		if (contextIndex != newContextIndex) {
			mapContent.moveLayer(contextIndex, newContextIndex);
			pane.redraw();
			Collections.swap(layersList, viewerIndex, newViewerIndex);
			mapLayerTableViewer.refresh();
		}

	}

	/**
	 * On show all layers.
	 */
	void onShowAllLayers() {
		if (pane != null && pane.getMapContent() != null) {
			for (final Layer layer : pane.getMapContent().layers()) {
				if (!layer.isVisible()) { layer.setVisible(true); }
			}
			mapLayerTableViewer.refresh();
			pane.redraw();
		}
	}

	/**
	 * On hide all layers.
	 */
	void onHideAllLayers() {
		if (pane != null && pane.getMapContent() != null) {
			for (final Layer layer : pane.getMapContent().layers()) {
				if (layer.isVisible()) { layer.setVisible(false); }
			}
			mapLayerTableViewer.refresh();
			pane.redraw();
		}
	}

	/**
	 * Gets the map layer table viewer.
	 *
	 * @return the map layer table viewer
	 */
	public MaplayerTableViewer getMapLayerTableViewer() { return mapLayerTableViewer; }

}
