package au.gov.ga.geodesy.port;

import java.io.Reader;

public interface SiteLogReaderFactory {
    boolean canCreate(Reader stream);
    SiteLogReader create(Reader stream);
}
