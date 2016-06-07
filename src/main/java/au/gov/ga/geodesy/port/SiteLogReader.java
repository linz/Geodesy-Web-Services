package au.gov.ga.geodesy.port;

import java.io.Reader;

import au.gov.ga.geodesy.domain.model.sitelog.SiteLog;

public abstract class SiteLogReader {

    protected Reader input;

    public SiteLogReader(Reader input) {
        this.input = input;
    }

    public abstract SiteLog getSiteLog() throws InvalidSiteLogException;
}
