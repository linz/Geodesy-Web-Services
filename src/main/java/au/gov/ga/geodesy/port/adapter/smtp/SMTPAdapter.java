package au.gov.ga.geodesy.port.adapter.smtp;

import au.gov.ga.geodesy.support.email.GeodesyMailMessage;

public interface SMTPAdapter {
    public void sendEmail(GeodesyMailMessage message);
}
