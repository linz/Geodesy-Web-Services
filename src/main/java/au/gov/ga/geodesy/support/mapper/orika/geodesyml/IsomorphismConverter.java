package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import au.gov.ga.geodesy.support.java.util.Isomorphism;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

/**
 * A bidirectional converter backed by an isomorphism. This adapter helps
 * us compose DTO mapper classes, like {@code SiteIdentificationMapper} and
 * {@code SiteLocationMapper}.
 *
 * @see au.gov.ga.geodesy.support.mapper.orika.geodesyml.SiteLogMapper
 */
public class IsomorphismConverter<A, B> extends BidirectionalConverter<A, B> {

    private Isomorphism<A, B> iso;

    public IsomorphismConverter(Isomorphism<A, B> iso) {
        this.iso = iso;
    }

    public B convertTo(A a, Type<B> targetType, MappingContext ctx) {
        return iso.to(a);
    }

    public A convertFrom(B b, Type<A> targetType, MappingContext ctx) {
        return iso.from(b);
    }
}
