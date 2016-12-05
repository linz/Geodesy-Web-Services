package au.gov.ga.geodesy.exception;

public class GeodesyRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 3216139083314642872L;

    public GeodesyRuntimeException() {
    }

    public GeodesyRuntimeException(String message) {
        super(message);
    }

    public GeodesyRuntimeException(Throwable cause) {
        super(cause);
    }

    public GeodesyRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public GeodesyRuntimeException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
