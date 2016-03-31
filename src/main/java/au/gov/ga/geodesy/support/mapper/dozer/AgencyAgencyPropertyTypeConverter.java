package au.gov.ga.geodesy.support.mapper.dozer;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.dozer.CustomConverter;
import org.dozer.MappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.gov.ga.geodesy.igssitelog.domain.model.Agency;
import au.gov.ga.geodesy.igssitelog.domain.model.Contact;
import au.gov.xml.icsm.geodesyml.v_0_3.AgencyPropertyType;
import au.gov.xml.icsm.geodesyml.v_0_3.ObjectFactory;
import net.opengis.iso19139.gco.v_20070417.CharacterStringPropertyType;
import net.opengis.iso19139.gmd.v_20070417.CIAddressPropertyType;
import net.opengis.iso19139.gmd.v_20070417.CIAddressType;
import net.opengis.iso19139.gmd.v_20070417.CIContactPropertyType;
import net.opengis.iso19139.gmd.v_20070417.CIContactType;
import net.opengis.iso19139.gmd.v_20070417.CIResponsiblePartyType;
import net.opengis.iso19139.gmd.v_20070417.CITelephonePropertyType;
import net.opengis.iso19139.gmd.v_20070417.CITelephoneType;
import net.opengis.iso19139.gmd.v_20070417.MDSecurityConstraintsType;

/**
 * Convert: au.gov.ga.geodesy.igssitelog.domain.model.Agency <--> au.gov.xml.icsm.geodesyml.v_0_3.AgencyPropertyType
 * 
 * ie. contacts
 */
public class AgencyAgencyPropertyTypeConverter implements CustomConverter {
    Logger logger = LoggerFactory.getLogger(getClass());
    ObjectFactory geoObjectFactory = new ObjectFactory();
    net.opengis.iso19139.gmd.v_20070417.ObjectFactory gmdObjectFactory = new net.opengis.iso19139.gmd.v_20070417.ObjectFactory();

    @SuppressWarnings("rawtypes")
    public Object convert(Object destination, Object source, Class destClass, Class sourceClass) {
        if (source == null) {
            return null;
        }
        if (source instanceof Agency) {
            Agency sourceType = (Agency) source;
            AgencyPropertyType dest = null;
            if (destination == null) {
                dest = geoObjectFactory.createAgencyPropertyType();
            } else {
                dest = (AgencyPropertyType) destination;
            }

            MDSecurityConstraintsType mdSecurityConstraints = gmdObjectFactory.createMDSecurityConstraintsType();
            dest.setMDSecurityConstraints(mdSecurityConstraints);

            CIResponsiblePartyType ciResponsibleParty = gmdObjectFactory.createCIResponsiblePartyType();
            dest.setCIResponsibleParty(ciResponsibleParty);

            CharacterStringPropertyType characterStringPropertyTypeName = DozerDelegate.mapWithGuard(
                    sourceType.getName(),
                    CharacterStringPropertyType.class);
            ciResponsibleParty.setOrganisationName(characterStringPropertyTypeName);

            CIAddressType ciAddressType = gmdObjectFactory.createCIAddressType();
            CIAddressPropertyType ciAddressPropertyType = gmdObjectFactory.createCIAddressPropertyType();
            CIContactType ciContactType = gmdObjectFactory.createCIContactType();
            ciAddressPropertyType.setCIAddress(ciAddressType);
            ciContactType.setAddress(ciAddressPropertyType);
            CIContactPropertyType ciContactPropertyType = gmdObjectFactory.createCIContactPropertyType();
            ciContactPropertyType.setCIContact(ciContactType);
            ciResponsibleParty.setContactInfo(ciContactPropertyType);
            if (sourceType.getPrimaryContact() != null) {
                CharacterStringPropertyType characterStringPropertyTypeIndividualName = DozerDelegate
                        .mapWithGuard(sourceType.getPrimaryContact().getName(), CharacterStringPropertyType.class);
                ciResponsibleParty.setIndividualName(characterStringPropertyTypeIndividualName);

                // c.getCIResponsibleParty().getContactInfo()
                // .getCIContact().getAddress().getCIAddress().getElectronicMailAddress().get(0)


                // Email
                CharacterStringPropertyType emailCSPT = DozerDelegate.mapWithGuard(
                        sourceType.getPrimaryContact().getEmail(),
                        CharacterStringPropertyType.class);
                ciAddressType.setElectronicMailAddress(Stream.of(emailCSPT).collect(Collectors.toList()));

                // Phone
                CITelephonePropertyType ciTelephonePropertyType = gmdObjectFactory.createCITelephonePropertyType();
                ciContactType.setPhone(ciTelephonePropertyType);
                CITelephoneType ciTelephoneType = gmdObjectFactory.createCITelephoneType();
                ciTelephonePropertyType.setCITelephone(ciTelephoneType);
                CharacterStringPropertyType phoneCSPT = DozerDelegate.mapWithGuard(
                        sourceType.getPrimaryContact().getTelephonePrimary(),
                        CharacterStringPropertyType.class);
                ciTelephoneType.setVoice(Stream.of(phoneCSPT).collect(Collectors.toList()));

                // Fax
                CharacterStringPropertyType faxCSPT = DozerDelegate.mapWithGuard(
                        sourceType.getPrimaryContact().getFax(),
                        CharacterStringPropertyType.class);
                ciTelephoneType.setFacsimile(Stream.of(faxCSPT).collect(Collectors.toList()));
                
            }
            // Address
            CharacterStringPropertyType mailingAddressCSPT = DozerDelegate.mapWithGuard(
                    sourceType.getMailingAddress(),
                    CharacterStringPropertyType.class);
            ciAddressType.setDeliveryPoint(Stream.of(mailingAddressCSPT).collect(Collectors.toList()));
            return dest;
        } else if (source instanceof AgencyPropertyType) {
            AgencyPropertyType sourceType = (AgencyPropertyType) source;
            Agency dest = null;
            if (destination == null) {
                dest = new Agency();
            } else {
                dest = (Agency) destination;
            }
            
            // A lot of effort to create Agency from AgencyPropertyType and not necessarily (in the current scope)
            
            return dest;
        } else {
            throw new MappingException("Converter TestCustomConverter " + "used incorrectly. Arguments passed in were:"
                    + destination + " and " + source);
        }
    }

}
