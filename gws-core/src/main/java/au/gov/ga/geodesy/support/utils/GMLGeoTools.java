package au.gov.ga.geodesy.support.utils;

import java.math.BigDecimal;

import au.gov.xml.icsm.geodesyml.v_0_5.SiteLocationType;

public class GMLGeoTools {

    /**
     * 
     * @param dms
     *            - 6 or 7 significant digits + fraction representing ddmmss.sss (Latitude) or dddmmss.ssss (Longitude)
     * @param easting
     *            - true then 7 digit dms (padding may be added in here to make so)
     * @return decimal degrees - -180 <= Longitude <= 180 / -90 <= Latitude <= 90
     */
    public static double dmsToDecmial(double dms) {
        boolean positiveSign = dms >= 0;
        long significantPart = (long) Math.abs(dms);
        double fractionPart = Math.abs(dms) - significantPart;

        String sigPartString = Long.toString(significantPart);
        int len = sigPartString.length();
        if (len > 7) {
            throw new RuntimeException("Expecting length x of DMS significant part to be x <= 7.  DMS: " + dms);
        }
        // Want to zero pad to 7 chars
        String zeros = len < 7 ? String.format("%0" + (7 - len) + "d", 0) : "";
        String formatted = zeros + sigPartString;

        // Breakup into degrees, minutes and seconds
        String[] dmsParts = new String[] {formatted.substring(0, 3), formatted.substring(3, 5),
                formatted.substring(5, 7)};

        double seconds = (Double.parseDouble(dmsParts[1]) * 60 + Double.parseDouble(dmsParts[2]) + fractionPart);
        double minutes = seconds / 3600;
        double decimal = Double.parseDouble(dmsParts[0]) + minutes;
        decimal = positiveSign ? decimal : -(decimal);
        return decimal;
    }
}
