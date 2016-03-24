package au.gov.ga.geodesy.port;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SiteLogReaderAbstractFactory {

    @Autowired
    private Collection<SiteLogReaderFactory> concreteFactories;

    public Optional<SiteLogReader> create(Reader input) {
        BufferedReader bufferedInput = new BufferedReader(input);
        return concreteFactories.stream()
            .filter(factory -> factory.canCreate(bufferedInput))
            .findFirst()
            .map(factory -> factory.create(bufferedInput));
    }
}

