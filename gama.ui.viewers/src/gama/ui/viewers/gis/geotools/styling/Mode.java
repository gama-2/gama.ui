/*******************************************************************************************************
 *
 * Mode.java, in gama.ui.shared.viewers, is part of the source code of the
 * GAMA modeling and simulation platform (v.1.9.0).
 *
 * (c) 2007-2022 UMI 209 UMMISCO IRD/SU & Partners (IRIT, MIAT, TLU, CTU)
 *
 * Visit https://github.com/gama-platform/gama for license information and contacts.
 * 
 ********************************************************************************************************/
package gama.ui.viewers.gis.geotools.styling;

/**
 * Captures the current *mode* of the style configurator making use
 * of a StyleViewer.
 * <p>
 * This is used to let the viewers get modey, and disable fill content
 * when working with linestrings for example.
 * </p>
 * 
 * @author Jody Garnett
 * @since 1.0.0
 *
 *
 *
 * @source $URL$
 */
public enum Mode { 
    /** <code>POINT</code> Mode - editing a Point or MultiPoint. */
    POINT, 
    /** <code>LINE</code> Mode - editing a Linestring or MultiLineString. */
    LINE, 
    /** <code>POLYGON</code> Mode - editing a Polygone or MultiPolygon. */
    POLYGON, 
    /** <code>ALL</code> Mode - editing a Geometry. */
    ALL, 
    /** <code>NONE</code> Mode - content cannot be styled by SLD (like scalebar) */
    NONE
}
