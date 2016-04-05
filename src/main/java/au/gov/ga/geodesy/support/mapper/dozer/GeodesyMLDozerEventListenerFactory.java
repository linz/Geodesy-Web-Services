package au.gov.ga.geodesy.support.mapper.dozer;

import org.dozer.DozerEventListener;
import org.dozer.event.DozerEvent;

public class GeodesyMLDozerEventListenerFactory implements DozerEventListener {

    @Override
    public void mappingStarted(DozerEvent event) {
    }

    @Override
    public void preWritingDestinationValue(DozerEvent event) {
    }

    @Override
    public void postWritingDestinationValue(DozerEvent event) {
        DozerEventListener handler = EventListenerFactory.getHandlerIfInteresting(event);
        handler.postWritingDestinationValue(event);
    }

    @Override
    public void mappingFinished(DozerEvent event) {
    }

    static class EventListenerFactory {

        /**
         * We may want to return a DozerEventListener to handle a specific element, such as for SiteLocationType to turn the countryCode
         * that is coming out as the country name from the translate into a code.
         * 
         * @param event
         * @return DozerEventListener that can handle if interesting or null if not.
         */
        public static DozerEventListener getHandlerIfInteresting(DozerEvent event) {
            switch (event.getDestinationObject().getClass().getSimpleName()) {
            case "SiteLocationType":
                return new GeodesyMLDozerEventListener_SiteLocationType();
            case "GnssReceiverType":
                return new GeodesyMLDozerEventListener_GnssReceiverType();
            }
            return new GeodesyMLDozerEventListener_noop();
        }

    }
}
