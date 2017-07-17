package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import au.gov.ga.geodesy.domain.model.sitelog.GnssReceiverLogItem;
import au.gov.ga.geodesy.support.java.util.Iso;
import au.gov.xml.icsm.geodesyml.v_0_4.GnssReceiverType;

@Component
public class GnssReceiverMapper implements Iso<GnssReceiverType, GnssReceiverLogItem> {

    @Autowired
    private GenericMapper mapper;

    public GnssReceiverLogItem to(GnssReceiverType receiver) {
        return mapper.map(receiver, GnssReceiverLogItem.class);
    }

    public GnssReceiverType from(GnssReceiverLogItem receiverLogItem) {
        return mapper.map(receiverLogItem, GnssReceiverType.class);
    }
}
