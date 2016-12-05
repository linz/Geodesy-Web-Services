package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import javax.xml.bind.JAXBElement;

import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLUtils;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

/**
 * A bidirectional converter where the source object is wrapped inside a
 * JAXBElement. We perform the wrapping and unwrapping, but delegate the
 * conversion of the nested object to the mapper facade.
 */
public class JAXBElementConverter<A, B> extends BidirectionalConverter<JAXBElement<A>, B> {

    /**
     * {@inheritDoc}
     */
    public B convertTo(JAXBElement<A> a, Type<B> targetType, MappingContext ctx) {
        return this.mapperFacade.map(a.getValue(), targetType.getRawType());
    }

    /**
     * {@inheritDoc}
     */
    public JAXBElement<A> convertFrom(B b, Type<JAXBElement<A>> targetType, MappingContext ctx) {
        @SuppressWarnings("unchecked")
        A a = this.mapperFacade.map(b, ((Type<A>) targetType.getActualTypeArguments()[0]).getRawType());
        return GeodesyMLUtils.wrapInJAXBElement(a);
    }
}
