package au.gov.ga.geodesy.support.mapper.dozer.populator;

import au.gov.ga.geodesy.support.utils.GMLGmlTools;
import au.gov.ga.geodesy.support.utils.GMLMiscTools;
import au.gov.xml.icsm.geodesyml.v_0_3.MoreInformationType;
import net.opengis.gml.v_3_2_1.CodeType;

public class MoreInformationTypePopulator extends GeodesyMLElementPopulator<MoreInformationType> {

    /**
     * Consider all required elements for this type and add any missing ones with default values.
     * 
     * @param gnssReceiverType
     */
    @Override
    public void checkAllRequiredElementsPopulated(MoreInformationType moreInformationType) {
        // This element can be blank when receiver hasn't been removed. Some other logic in the project
        // removes empty elements from the Sopac SiteLog before it gets to this translator
        checkElementPopulated(moreInformationType, "dataCenter", GMLMiscTools.getEmptyList(String.class));
        checkElementPopulated(moreInformationType, "urlForMoreInformation", GMLMiscTools.getEmptyString());
        checkElementPopulated(moreInformationType, "siteMap", GMLMiscTools.getEmptyString());
        checkElementPopulated(moreInformationType, "siteDiagram", GMLMiscTools.getEmptyString());
        checkElementPopulated(moreInformationType, "sitePictures", GMLMiscTools.getEmptyString());
        checkElementPopulated(moreInformationType, "monumentDescription", GMLMiscTools.getEmptyString());
        checkElementPopulated(moreInformationType, "antennaGraphicsWithDimensions", GMLMiscTools.getEmptyString());
        checkElementPopulated(moreInformationType, "insertTextGraphicFromAntenna", GMLMiscTools.getEmptyString());
        // TODO fix up CodeValue
        CodeType codeType = GMLGmlTools.getEmptyCodeType();
        codeType.setValue("CodeTypeTODO");
        codeType.setCodeSpace("CodeSpaceTODO");
        checkElementPopulated(moreInformationType, "DOI", codeType);
    }
}
