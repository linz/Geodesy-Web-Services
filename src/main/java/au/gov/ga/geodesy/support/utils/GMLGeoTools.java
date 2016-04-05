package au.gov.ga.geodesy.support.utils;

public class GMLGeoTools {
    public static final boolean easting = true;
    public static final boolean northing = false;

    /**
     * 
     * @param dms
     *            - 6 or 7 significant digits + fraction representing ddmmss.sss (Northings) or dddmmss.ssss (Eastings)
     * @param easting - true then 7 digit dms (padding may be added in here to make so)
     * @return decimal degrees -  -180 <= northlings <= 180 / -90 <= Eastings <= 90
     */
    public static Double dmsToDecmial(double dms, boolean easting) {
        boolean positiveSign = dms >= 0;
        long significantPart = (long) Math.abs(dms);
        double fractionPart = Math.abs(dms) - significantPart;

        String sigPartString = Long.toString(significantPart);
        int origlen = sigPartString.length();
        if (origlen > 7) {
            throw new RuntimeException("Expecting length x of DMS significant part to be x <= 7.  DMS: " + dms);
        }
        // Want to make sure is 7 chars for Easting and 6 for Northing with leading zero padding
        int len = easting ? 7 : 6;
        String zeros = len>origlen ? String.format("%0"+(len-origlen)+"d", 0) : "";
        String formatted = zeros+sigPartString;

        // Breakup into degrees, minutes and seconds
        String[] dmsParts = new String[] {formatted.substring(0, len-4), formatted.substring(len-4, len-4+2),
                formatted.substring(len-4+2, len-4+4)};

        double seconds = (Double.parseDouble(dmsParts[1]) * 60 + Double.parseDouble(dmsParts[2]) + fractionPart);
        double minutes = seconds / 3600;
        double decimal = Double.parseDouble(dmsParts[0]) + minutes;
        decimal = positiveSign ? decimal : -(decimal);
        return decimal;
    }

}
