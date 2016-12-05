package au.gov.ga.geodesy.port;

import java.io.IOException;
import java.io.Reader;

import au.gov.ga.geodesy.domain.model.sitelog.SiteLog;
import org.apache.commons.io.IOUtils;

public abstract class SiteLogReader {

    private Reader input;

    public SiteLogReader(Reader input) {
        this.input = input;
    }

    /**
     * Unmarshal the given string into a site log.
     */
    protected abstract SiteLog getSiteLog(String siteLogText) throws InvalidSiteLogException;

    /**
     * {@inheritDoc}
     */
    public SiteLog getSiteLog() throws InvalidSiteLogException {
        try {
            String siteLogText = IOUtils.toString(input);
            SiteLog siteLog = getSiteLog(siteLogText);
            siteLog.setSiteLogText(siteLogText);
            return siteLog;
        }
        catch (IOException e) {
            throw new InvalidSiteLogException(e);
        }
    }
}
