package au.gov.ga.geodesy.support.mapper.dozer.converter;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import au.gov.ga.geodesy.igssitelog.domain.model.MoreInformation;
import au.gov.ga.geodesy.support.utils.GMLMiscTools;
import au.gov.xml.icsm.geodesyml.v_0_3.MoreInformationType;

/**
 * This is a class independant of the style of all the other Converters.
 * 
 * au.gov.ga.geodesy.igssitelog.domain.model.MoreInformation
 * <--> au.gov.xml.icsm.geodesyml.v_0_3.MoreInformationType
 * 
 * All fields are exactly the same between these types EXCEPT for the dataCenters. Dozer doesn't seem to let FieldMapping happen first
 * and then Converter. So I perform the DataCenter conversion outside of Dozer.
 * 
 * @author brookes
 *
 */
public class MoreInformationAfterMapping {

    public static void fixMoreInformation(MoreInformation moreInformation, MoreInformationType moreInformationType) {
        List<String> dataCenters = GMLMiscTools.getEmptyList(String.class);
        String primary = moreInformation.getPrimaryDataCenter();
        String secondary = moreInformation.getSecondaryDataCenter();
        dataCenters.add(StringUtils.isBlank(primary) ? "" : primary);
        if (StringUtils.isBlank(secondary)) {
            // nothing
        } else {
            dataCenters.add(secondary);
        }
        moreInformationType.setDataCenter(dataCenters);
    }

}
