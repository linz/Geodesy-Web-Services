package au.gov.ga.geodesy.port;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SiteLogReaderAbstractFactory {

    private final Logger log = LoggerFactory.getLogger(SiteLogReaderAbstractFactory.class);

    @Autowired
    private Collection<SiteLogReaderFactory> concreteFactories;

    public Optional<SiteLogReader> create(Reader input) {
        try {
            BufferedReader bufferedInput = new BufferedReader(input);
            for (SiteLogReaderFactory f : concreteFactories) {
                // TODO: adjust read limit
                bufferedInput.mark(100000);
                if (f.canRecogniseInput(bufferedInput)) {
                    bufferedInput.reset();
                    return Optional.of(f.create(bufferedInput));
                }
            }
            return Optional.empty();
        }
        catch (IOException e) {
            log.warn("Failed to choose concrete site log reader factory", e);
            return Optional.empty();
        }
    }
}

