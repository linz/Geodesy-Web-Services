package au.gov.ga.geodesy.support.mapper.dozer.populator;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dozer.event.DozerEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.gov.ga.geodesy.support.utils.GMLGeoTools;
import au.gov.ga.geodesy.support.utils.GMLGmlTools;
import au.gov.ga.geodesy.support.utils.GMLMiscTools;
import au.gov.xml.icsm.geodesyml.v_0_3.SiteLocationType;

/**
 * The translate simply copied the Countries (in Sopac Sitelog XML) across to the GeoesyML CountryCode elements.
 * This cleans those up by converting to the actual CountryCodes.
 * 
 * @author brookes
 *
 */
public class SiteLocationTypePopulator extends GeodesyMLElementPopulator<SiteLocationType> {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    void checkAllRequiredElementsPopulated(SiteLocationType siteLocationType) {
        checkElementPopulated(siteLocationType, "city", GMLMiscTools.getEmptyString());
        checkElementPopulated(siteLocationType, "state", GMLMiscTools.getEmptyString());
        checkElementPopulated(siteLocationType, "countryCodeISO", GMLMiscTools.getEmptyString());
        checkElementPopulated(siteLocationType, "tectonicPlate", GMLGmlTools.getEmptyCodeType());
        checkElementPopulated(siteLocationType, "approximatePositionITRF",
                GMLGeoTools.buildZeroApproximatePositionITRF());
    }

    @Override
    public void postWritingDestinationValue(DozerEvent event) {
        super.postWritingDestinationValue(event);

        SiteLocationType siteLocationType = (SiteLocationType) event.getDestinationObject();
        String country = siteLocationType.getCountryCodeISO();
        String code = COUNTRY_CODES_ALPHA_3.lookupCode(country);
        siteLocationType.setCountryCodeISO(code);
        logger.debug(String.format("Change country: '%s' to code '%s'", country, code));
    }

    /**
     * Data from https://en.wikipedia.org/wiki/ISO_3166-1_alpha-3
     * 
     * This is used to map between any code and country and vice-versa
     * 
     * @author brookes
     *
     */
    public static class COUNTRY_CODES_ALPHA_3 {
        private static Map<String, String> codeToCountry = new HashMap<>();
        private static Map<String, String> countryToCode = new HashMap<>();
        private static String[] codesArrayList = new String[] {"ABW", "Aruba", "AFG", "Afghanistan", "AGO", "Angola",
                "AIA", "Anguilla", "ALA", "Åland Islands", "ALB", "Albania", "AND", "Andorra", "ARE",
                "United Arab Emirates", "ARG", "Argentina", "ARM", "Armenia", "ASM", "American Samoa", "ATA",
                "Antarctica", "ATF", "French Southern Territories", "ATG", "Antigua and Barbuda", "AUS", "Australia",
                "AUT", "Austria", "AZE", "Azerbaijan", "BDI", "Burundi", "BEL", "Belgium", "BEN", "Benin", "BES",
                "Bonaire, Sint Eustatius and Saba", "BFA", "Burkina Faso", "BGD", "Bangladesh", "BGR", "Bulgaria",
                "BHR", "Bahrain", "BHS", "Bahamas", "BIH", "Bosnia and Herzegovina", "BLM", "Saint Barthélemy", "BLR",
                "Belarus", "BLZ", "Belize", "BMU", "Bermuda", "BOL", "Bolivia, Plurinational State of", "BRA", "Brazil",
                "BRB", "Barbados", "BRN", "Brunei Darussalam", "BTN", "Bhutan", "BVT", "Bouvet Island", "BWA",
                "Botswana", "CAF", "Central African Republic", "CAN", "Canada", "CCK", "Cocos (Keeling) Islands", "CHE",
                "Switzerland", "CHL", "Chile", "CHN", "China", "CIV", "Côte d'Ivoire", "CMR", "Cameroon", "COD",
                "Congo, the Democratic Republic of the", "COG", "Congo", "COK", "Cook Islands", "COL", "Colombia",
                "COM", "Comoros", "CPV", "Cabo Verde", "CRI", "Costa Rica", "CUB", "Cuba", "CUW", "Curaçao", "CXR",
                "Christmas Island", "CYM", "Cayman Islands", "CYP", "Cyprus", "CZE", "Czech Republic", "DEU", "Germany",
                "DJI", "Djibouti", "DMA", "Dominica", "DNK", "Denmark", "DOM", "Dominican Republic", "DZA", "Algeria",
                "ECU", "Ecuador", "EGY", "Egypt", "ERI", "Eritrea", "ESH", "Western Sahara", "ESP", "Spain", "EST",
                "Estonia", "ETH", "Ethiopia", "FIN", "Finland", "FJI", "Fiji", "FLK", "Falkland Islands (Malvinas)",
                "FRA", "France", "FRO", "Faroe Islands", "FSM", "Micronesia, Federated States of", "GAB", "Gabon",
                "GBR", "United Kingdom", "GEO", "Georgia", "GGY", "Guernsey", "GHA", "Ghana", "GIB", "Gibraltar", "GIN",
                "Guinea", "GLP", "Guadeloupe", "GMB", "Gambia", "GNB", "Guinea-Bissau", "GNQ", "Equatorial Guinea",
                "GRC", "Greece", "GRD", "Grenada", "GRL", "Greenland", "GTM", "Guatemala", "GUF", "French Guiana",
                "GUM", "Guam", "GUY", "Guyana", "HKG", "Hong Kong", "HMD", "Heard Island and McDonald Islands", "HND",
                "Honduras", "HRV", "Croatia", "HTI", "Haiti", "HUN", "Hungary", "IDN", "Indonesia", "IMN",
                "Isle of Man", "IND", "India", "IOT", "British Indian Ocean Territory", "IRL", "Ireland", "IRN",
                "Iran, Islamic Republic of", "IRQ", "Iraq", "ISL", "Iceland", "ISR", "Israel", "ITA", "Italy", "JAM",
                "Jamaica", "JEY", "Jersey", "JOR", "Jordan", "JPN", "Japan", "KAZ", "Kazakhstan", "KEN", "Kenya", "KGZ",
                "Kyrgyzstan", "KHM", "Cambodia", "KIR", "Kiribati", "KNA", "Saint Kitts and Nevis", "KOR",
                "Korea, Republic of", "KWT", "Kuwait", "LAO", "Lao People's Democratic Republic", "LBN", "Lebanon",
                "LBR", "Liberia", "LBY", "Libya", "LCA", "Saint Lucia", "LIE", "Liechtenstein", "LKA", "Sri Lanka",
                "LSO", "Lesotho", "LTU", "Lithuania", "LUX", "Luxembourg", "LVA", "Latvia", "MAC", "Macao", "MAF",
                "Saint Martin (French part)", "MAR", "Morocco", "MCO", "Monaco", "MDA", "Moldova, Republic of", "MDG",
                "Madagascar", "MDV", "Maldives", "MEX", "Mexico", "MHL", "Marshall Islands", "MKD",
                "Macedonia, the former Yugoslav Republic of", "MLI", "Mali", "MLT", "Malta", "MMR", "Myanmar", "MNE",
                "Montenegro", "MNG", "Mongolia", "MNP", "Northern Mariana Islands", "MOZ", "Mozambique", "MRT",
                "Mauritania", "MSR", "Montserrat", "MTQ", "Martinique", "MUS", "Mauritius", "MWI", "Malawi", "MYS",
                "Malaysia", "MYT", "Mayotte", "NAM", "Namibia", "NCL", "New Caledonia", "NER", "Niger", "NFK",
                "Norfolk Island", "NGA", "Nigeria", "NIC", "Nicaragua", "NIU", "Niue", "NLD", "Netherlands", "NOR",
                "Norway", "NPL", "Nepal", "NRU", "Nauru", "NZL", "New Zealand", "OMN", "Oman", "PAK", "Pakistan", "PAN",
                "Panama", "PCN", "Pitcairn", "PER", "Peru", "PHL", "Philippines", "PLW", "Palau", "PNG",
                "Papua New Guinea", "POL", "Poland", "PRI", "Puerto Rico", "PRK",
                "Korea, Democratic People's Republic of", "PRT", "Portugal", "PRY", "Paraguay", "PSE",
                "Palestine, State of", "PYF", "French Polynesia", "QAT", "Qatar", "REU", "Réunion", "ROU", "Romania",
                "RUS", "Russian Federation", "RWA", "Rwanda", "SAU", "Saudi Arabia", "SDN", "Sudan", "SEN", "Senegal",
                "SGP", "Singapore", "SGS", "South Georgia and the South Sandwich Islands", "SHN",
                "Saint Helena, Ascension and Tristan da Cunha", "SJM", "Svalbard and Jan Mayen", "SLB",
                "Solomon Islands", "SLE", "Sierra Leone", "SLV", "El Salvador", "SMR", "San Marino", "SOM", "Somalia",
                "SPM", "Saint Pierre and Miquelon", "SRB", "Serbia", "SSD", "South Sudan", "STP",
                "Sao Tome and Principe", "SUR", "Suriname", "SVK", "Slovakia", "SVN", "Slovenia", "SWE", "Sweden",
                "SWZ", "Swaziland", "SXM", "Sint Maarten (Dutch part)", "SYC", "Seychelles", "SYR",
                "Syrian Arab Republic", "TCA", "Turks and Caicos Islands", "TCD", "Chad", "TGO", "Togo", "THA",
                "Thailand", "TJK", "Tajikistan", "TKL", "Tokelau", "TKM", "Turkmenistan", "TLS", "Timor-Leste", "TON",
                "Tonga", "TTO", "Trinidad and Tobago", "TUN", "Tunisia", "TUR", "Turkey", "TUV", "Tuvalu", "TWN",
                "Taiwan, Province of China", "TZA", "Tanzania, United Republic of", "UGA", "Uganda", "UKR", "Ukraine",
                "UMI", "United States Minor Outlying Islands", "URY", "Uruguay", "USA", "United States of America",
                "UZB", "Uzbekistan", "VAT", "Holy See (Vatican City State)", "VCT", "Saint Vincent and the Grenadines",
                "VEN", "Venezuela, Bolivarian Republic of", "VGB", "Virgin Islands, British", "VIR",
                "Virgin Islands, U.S.", "VNM", "Viet Nam", "VUT", "Vanuatu", "WLF", "Wallis and Futuna", "WSM", "Samoa",
                "YEM", "Yemen", "ZAF", "South Africa", "ZMB", "Zambia", "ZWE", "Zimbabwe", "???", "???",
                // Added from some other source
                "PYF", "Tahiti", "KGZ", "Kyrghyzstan", "TF", "Kerguelen Islands", "KZ", "Kazakstan"};

        static {
            if (codesArrayList.length % 2 != 0) {
                throw new RuntimeException("codesArrayList has changed - it should be even (code, country)*");
            }
            for (int i = 0; i < codesArrayList.length; i += 2) {
                codeToCountry.put(codesArrayList[i].toUpperCase(), codesArrayList[i + 1].toUpperCase());
                countryToCode.put(codesArrayList[i + 1].toUpperCase(), codesArrayList[i].toUpperCase());
            }
        }

        public static String lookupCode(String country) {
            if (StringUtils.isBlank(country)) {
                return "???";
            }
            String modCountry = fixUpProblemCountry(country).toUpperCase();
            if (countryToCode.containsKey(modCountry)) {
                return countryToCode.get(country.toUpperCase());
            } else {
                return country;
            }
        }

        /**
         * Some country strings are incorrect - fix to something that is.
         * 
         * @param upperCase
         * @return
         */
        private static String fixUpProblemCountry(String inputCountry) {
            if (StringUtils.isBlank(inputCountry)) {
                return "???";
            }
            switch (inputCountry.toUpperCase()) {
            case "ANTARCTIC PENINSULA":
                return "Antarctic";
            case "ASCENSION ISLAND":
                return "Saint Helena, Ascension and Tristan da Cunha";
            case "FRENCH GUYANA":
                return "Guyana";
            case "GREENLAND (DENMARK)":
            case "GREENLAND(DENMARK)":
                return "Greenland";
            case "IRAN":
                return "Iran, Islamic Republic of";
            case "KOREA":
                return "Korea, Republic of";
            case "MICRONESIA (FEDERATED STATES OF)":
                return "Micronesia, Federated States of";
            case "NEGARA BRUNEI DARUSSALAM":
                return "Brunei Darussalam";
            case "P.R. CHINA":
            case "REPUBLIC OF CHINA":
            case "P.R.C.":
                return "China";
            case "RUSSIA":
                return "Russian Federation";
            case "REPUBLIC OF ARMENIA":
                return "Armenia";
            case "REPUBLIC OF MALDIVES":
            case "REPUBLIC OF SINGAPORE":
            case "REPUBLIC OF SOUTH AFRICA":
                return "Russian Federation";
            case "SOUTH KOREA":
                return "Korea, Democratic People's Republic of";
            case "SOUTHERN OCEAN":
                return "French Southern Territories";
            case "TAHITI":
            case "TAHITI, FRENCH POLYNESIA":
                return "Tahiti";
            case "U.K. TERRITORY":
            case "UK":
            case "DEPENDENT TERRITORY OF THE U.K.":
                return "United Kingdom";
            case "U.S. VIRGIN ISLANDS (USA)":
                return "Virgin Islands, U.S";
            case "U.S.A.":
            case "UNITED STATES":
                return "United States of America";
            default:
                return inputCountry;
            }
        }

        public static String lookupCountry(String code) {
            if (StringUtils.isBlank(code)) {
                return "???";
            }
            if (codeToCountry.containsKey(code.toUpperCase())) {
                return codeToCountry.get(code.toUpperCase());
            } else {
                return code;
            }
        }
    }
}
