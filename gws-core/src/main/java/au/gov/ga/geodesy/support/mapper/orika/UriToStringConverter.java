package au.gov.ga.geodesy.support.mapper.orika;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

public class UriToStringConverter extends BidirectionalConverter<URI, String> {

    public String convertTo(URI uri, Type<String> destType, MappingContext ctx) {
        try {
            return uri.toURL().toExternalForm();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public URI convertFrom(String uri, Type<URI> destType, MappingContext ctx) {
        try {
            return new URI(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
