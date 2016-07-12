package au.gov.ga.geodesy.support.mapper.dozer.converter;

import au.gov.ga.geodesy.igssitelog.domain.model.MoreInformation;
import au.gov.xml.icsm.geodesyml.v_0_3.MoreInformationType;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;


public class MoreInformationAfterMappingTest {
    @Test
    public void testMoreInformationIsNull() {
        MoreInformation moreInformation = null;
        MoreInformationType moreInformationType = new MoreInformationType();
        MoreInformationAfterMapping.fixMoreInformation(moreInformation, moreInformationType);

        assertThat(moreInformationType.getDataCenter(), notNullValue());
        assertThat(moreInformationType.getDataCenter().size(), is(1));
        assertThat(moreInformationType.getDataCenter().get(0), is(""));

        assertThat(moreInformationType.getUrlForMoreInformation(), notNullValue());
        assertThat(moreInformationType.getSiteMap(), notNullValue());
        assertThat(moreInformationType.getMonumentDescription(), notNullValue());
        assertThat(moreInformationType.getAntennaGraphicsWithDimensions(), notNullValue());
        assertThat(moreInformationType.getInsertTextGraphicFromAntenna(), notNullValue());
    }

}
