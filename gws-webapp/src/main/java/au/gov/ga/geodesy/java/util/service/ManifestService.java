package au.gov.ga.geodesy.java.util.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * Service for retrieving Java JAR / WAR Manifest information.
 */
@Component
public class ManifestService {
    private static final Logger logger = LoggerFactory.getLogger(ManifestService.class);
    private static final String INTERNAL_PROJECT = "au.gov.ga.geodesy";

    private Map<String, GAManifestData> internalManifestData;
    private Map<String, GAManifestData> externalManifestData;

    private ServletContext servletContext;

    public ManifestService() {
        init();
    }

    void init() {
        internalManifestData = new HashMap<>();
        externalManifestData = new HashMap<>();
        buildManifests();
    }

    /**
     * @return the manifest information for the Geodesy-related Jars
     */
    public Map<String, GAManifestData> getInternalManifests() {
        return internalManifestData;
    }

    /**
     * @return the manifest information for the Jars external to Geodesy
     */
    public Map<String, GAManifestData> getExternalManifests() {
        return externalManifestData;
    }

    void buildManifests() {
        Enumeration manifestsEnum;
        try {
            manifestsEnum = Thread.currentThread().getContextClassLoader().getResources(JarFile.MANIFEST_NAME);
            while (manifestsEnum.hasMoreElements()) {
                try {
                    URL url = (URL) manifestsEnum.nextElement();
                    buildManifest(url);
                } catch (Exception e) {
                    throw new RuntimeException("Error with manifests enumeration", e);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error with reading Manifest information", e);
        }
    }

    public void buildManifest(URL url) {
        if (url == null) {
            return;
        }
        GAManifestData gaManifestData = new GAManifestData(url);
        if (gaManifestData.isInternalManifest()) {
            internalManifestData.put(extractAppName(gaManifestData.getManifestUrl().toString()), gaManifestData);
        } else {
            externalManifestData.put(extractAppName(gaManifestData.getManifestUrl().toString()), gaManifestData);
        }
    }

    String extractAppName(String manifestPath) {
        return manifestPath.replaceAll(".+?/([^/]*?/META-INF/MANIFEST\\.MF)", "$1");
    }

    /**
     * Extract the important parts from a Manifest
     */
    public class GAManifestData {
        private Map<String, String> manifest;
        @JsonIgnore
        private URL manifestUrl;

        public GAManifestData(URL manifestUrl) {
            this.manifestUrl = manifestUrl;
            Manifest manifest = null;
            try (InputStream is = manifestUrl.openStream()) {
                manifest = new Manifest(is);
                buildManifestInfo(manifest);
            } catch (IOException e) {
                logger.error("Error reading InputStream from manifest URL: " + manifestUrl, e);
            }
        }


        private void buildManifestInfo(Manifest manifest) {
            this.manifest = new HashMap<>();
            Attributes mainAttribs = manifest.getMainAttributes();
            mainAttribs.forEach((k, v) -> {
                Attributes.Name key = k instanceof Attributes.Name ? (Attributes.Name) k : Attributes.Name.SPECIFICATION_VERSION;
                String value = v instanceof String ? (String) v : "not string value";
                this.manifest.put(key.toString(), updateManifest(key.toString(), value));
            });
        }

        /**
         * Opportunity to change values, such as force dates into the local timezone
         *
         * @param key   - from the manifest
         * @param value - value from the manifest that can be modified
         * @return the modified (if at all) Value
         */
        private String updateManifest(String key, String value) {
            switch (key.toLowerCase()) {
                case "build-date": {
                    DateTimeFormatter inFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");
                    ZonedDateTime buildDate = ZonedDateTime.from(inFormatter.parse(value));
                    OffsetDateTime outDate = buildDate.toOffsetDateTime();
                    ZonedDateTime zonedDateTime = outDate.atZoneSameInstant(ZoneId.systemDefault());
                    String outDateString = zonedDateTime.format(inFormatter);
                    logger.debug("updateManifest - key: " + key + "; in - " + value + ", out - " + outDateString);
                    return outDateString;
                }
                default:
                    return value;
            }
        }

        /**
         * @return Implementation-Vendor if exists or Implementation-Vendor-Id if exists otherwise path to Manifest file
         */
        @JsonIgnore
        public String getImplementationVendor() {
            String implVendor = manifest.containsKey("Implementation-Vendor") ? manifest.get("Implementation-Vendor") : "";
            String implVendorId = manifest.containsKey("Implementation-Vendor-Id") ? manifest.get("Implementation-Vendor-Id") : "";
            if (!StringUtils.isBlank(implVendor)) {
                return implVendor;
            } else if (!StringUtils.isBlank(implVendorId)) {
                return implVendorId;
            } else {
                return manifestUrl.toString();
            }
        }

        @JsonIgnore
        public boolean isInternalManifest() {
            boolean isInternal = this.getImplementationVendor().contains(INTERNAL_PROJECT);
            if (logger.isDebugEnabled()) {
                logger.debug("** url: " + this.getManifestUrl());
                logger.debug("  getImplementationVendor(): " + this.getImplementationVendor());
                logger.debug("  internal?: " + isInternal);
            }
            return isInternal;
        }


        public Map<String, String> getManifest() {
            return manifest;
        }

        public URL getManifestUrl() {
            return manifestUrl;
        }
    }
}