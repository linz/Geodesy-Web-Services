package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import au.gov.ga.geodesy.exception.GeodesyRuntimeException;
import au.gov.ga.geodesy.support.gml.GMLPropertyType;
import au.gov.ga.geodesy.support.java.util.Iso;

import net.opengis.gml.v_3_2_1.AbstractGMLType;

/**
 * Reversible mapping between a GML property type and its target element.
 */
public class GMLPropertyTypeMapper<P extends GMLPropertyType, T extends AbstractGMLType> implements Iso<P, T> {

    public T to(P p) {
        return p.getTargetElement();
    }

    public P from(T t) {
        try {
            String elementTypeName = t.getClass().getName();
            String propertyTypeName = elementTypeName.substring(0, elementTypeName.length() - 4) + "PropertyType";
            @SuppressWarnings("unchecked")
            P p = (P) Class.forName(propertyTypeName).newInstance();
            p.setTargetElement(t);
            return p;
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new GeodesyRuntimeException("Failed to wrap " + t.getClass().getSimpleName() + " in GML property type", e);
        }
    }
}

