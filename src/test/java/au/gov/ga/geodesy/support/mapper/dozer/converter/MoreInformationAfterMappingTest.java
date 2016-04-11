package au.gov.ga.geodesy.support.mapper.dozer.converter;

import org.junit.Assert;
import org.junit.Test;

import au.gov.ga.geodesy.igssitelog.domain.model.MoreInformation;
import au.gov.xml.icsm.geodesyml.v_0_3.MoreInformationType;

public class MoreInformationAfterMappingTest {
    @Test
    public void testMoreInformationIsNull() {
        MoreInformation moreInformation = null;
        MoreInformationType moreInformationType = new MoreInformationType();
        MoreInformationAfterMapping.fixMoreInformation(moreInformation, moreInformationType);

        Assert.assertNotNull(moreInformationType.getDataCenter());
        Assert.assertEquals(1, moreInformationType.getDataCenter().size());
        Assert.assertEquals("", moreInformationType.getDataCenter().get(0));

        Assert.assertNotNull(moreInformationType.getUrlForMoreInformation());
        Assert.assertNotNull(moreInformationType.getSiteMap());
        Assert.assertNotNull(moreInformationType.getMonumentDescription());
        Assert.assertNotNull(moreInformationType.getAntennaGraphicsWithDimensions());
        Assert.assertNotNull(moreInformationType.getInsertTextGraphicFromAntenna());
    }

}
