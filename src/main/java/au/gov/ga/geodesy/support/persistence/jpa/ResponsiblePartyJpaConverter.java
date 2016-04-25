package au.gov.ga.geodesy.support.persistence.jpa;

import java.io.StringReader;
import java.io.StringWriter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.opengis.metadata.citation.ResponsibleParty;

import au.gov.ga.geodesy.port.adapter.geodesyml.GeodesyMLMarshaller;
import au.gov.ga.geodesy.port.adapter.geodesyml.MarshallingException;
import au.gov.ga.geodesy.support.mapper.orika.ResponsiblePartyOrikaMapper;
import au.gov.ga.geodesy.support.marshalling.moxy.GeodesyMLMoxy;

import net.opengis.iso19139.gmd.v_20070417.CIResponsiblePartyType;

@Converter
// TODO: Can JPA converters be @Configurable? It would be good to autowire the marshaller
// and the mapper.
public class ResponsiblePartyJpaConverter implements AttributeConverter<ResponsibleParty, String> {

    private ResponsiblePartyOrikaMapper mapper = new ResponsiblePartyOrikaMapper();
    private GeodesyMLMarshaller marshaller;

    public ResponsiblePartyJpaConverter() throws MarshallingException {
        marshaller = new GeodesyMLMoxy();
    }

    private String marshal(Object x) {
        try {
            StringWriter writer = new StringWriter();
            marshaller.marshal(x, writer);
            return writer.toString();
        }
        catch (MarshallingException e) {
            throw new RuntimeException(e);
        }
    }

    private CIResponsiblePartyType unmarshal(String ml) {
        try {
            return marshaller.unmarshal(new StringReader(ml), CIResponsiblePartyType.class).getValue();
        }
        catch (MarshallingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String convertToDatabaseColumn(ResponsibleParty party) {
        String s = marshal(mapper.mapToDto(party));
        /* System.out.println(s); */
        return s;
    }

    @Override
    public ResponsibleParty convertToEntityAttribute(String ml) {
        return mapper.mapFromDto(unmarshal(ml));
    }
}
