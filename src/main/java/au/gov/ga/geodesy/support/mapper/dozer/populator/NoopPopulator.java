package au.gov.ga.geodesy.support.mapper.dozer.populator;

import org.dozer.DozerEventListener;
import org.dozer.event.DozerEvent;

/**
 * This is a dummy that does nothing.
 * 
 * @author brookes
 *
 */
public class NoopPopulator implements DozerEventListener {

    @Override
    public void mappingStarted(DozerEvent event) {
    }

    @Override
    public void preWritingDestinationValue(DozerEvent event) {
    }

    @Override
    public void postWritingDestinationValue(DozerEvent event) {
    }

    @Override
    public void mappingFinished(DozerEvent event) {
    }

}
