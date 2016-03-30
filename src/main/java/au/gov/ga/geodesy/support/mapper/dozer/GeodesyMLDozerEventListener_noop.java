package au.gov.ga.geodesy.support.mapper.dozer;

import org.dozer.DozerEventListener;
import org.dozer.event.DozerEvent;

/**
 * This is a dummy that should do nothing.
 * 
 * @author brookes
 *
 */
public class GeodesyMLDozerEventListener_noop implements DozerEventListener {

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
