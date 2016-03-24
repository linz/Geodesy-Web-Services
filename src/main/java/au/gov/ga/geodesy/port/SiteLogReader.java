package au.gov.ga.geodesy.port;

import java.io.Reader;

public abstract class SiteLogReader implements SiteLogSource {

    protected Reader input;

    public SiteLogReader(Reader input) {
        this.input = input;
    }
}
