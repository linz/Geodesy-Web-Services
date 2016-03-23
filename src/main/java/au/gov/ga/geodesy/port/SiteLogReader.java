package au.gov.ga.geodesy.port;

import java.io.Reader;

public abstract class SiteLogReader implements SiteLogSource {

    protected Reader reader;

    public SiteLogReader(Reader r) {
        reader = r;
    }
}
