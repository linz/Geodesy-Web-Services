package au.gov.ga.geodesy.port.adapter.sopac;

import java.io.Reader;

import au.gov.ga.geodesy.port.SiteLogReader;
import au.gov.ga.geodesy.port.SiteLogReaderFactory;

public class SiteLogSopacReaderFactory implements SiteLogReaderFactory {

	@Override
	public boolean canCreate(Reader input) {
		// TODO: implement decision
		return true;
	}

	@Override
	public SiteLogReader create(Reader input) {
        return new SiteLogSopacReader(input);
	}
}
