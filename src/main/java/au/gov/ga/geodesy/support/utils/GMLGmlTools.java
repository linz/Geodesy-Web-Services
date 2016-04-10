package au.gov.ga.geodesy.support.utils;

import java.util.Arrays;
import java.util.stream.Collectors;

import net.opengis.gml.v_3_2_1.CodeType;
import net.opengis.gml.v_3_2_1.TimePositionType;

/**
 * Tools and Utils to help with GML types (as in import net.opengis.gml, not GeodesyML)
 * 
 * @author brookes
 *
 */
public class GMLGmlTools {
    public static TimePositionType getEmptyTimePositionType() {
        TimePositionType tpt = new TimePositionType();
        tpt.setValue(Arrays.stream(new String[] {""}).collect(Collectors.toList()));
        return tpt;
    }

    public static CodeType getEmptyCodeType() {
        CodeType codeType = new CodeType();
        codeType.setValue("");
        codeType.setCodeSpace("");
        return codeType;
    }
}
