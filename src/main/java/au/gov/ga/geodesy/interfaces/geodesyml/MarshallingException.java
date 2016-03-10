package au.gov.ga.geodesy.interfaces.geodesyml;

@SuppressWarnings("serial")
public class MarshallingException extends Exception {

    public MarshallingException(String message) {
        super(message);
    }

    public MarshallingException(String message, Throwable x) {
        super(message, x);
    }
}
