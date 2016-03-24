package au.gov.ga.geodesy.port;

@SuppressWarnings("serial")
public class InvalidSiteLogException extends Exception {

    public InvalidSiteLogException(String message) {
        super(message);
    }

    public InvalidSiteLogException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidSiteLogException(Throwable cause) {
        super(cause);
    }
}
