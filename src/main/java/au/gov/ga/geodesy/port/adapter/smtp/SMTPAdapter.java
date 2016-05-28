package au.gov.ga.geodesy.port.adapter.smtp;

import au.gov.ga.geodesy.support.email.GeodesyMailMessage;

// TODO: remove this interface and use JavaMailSender directly
public interface SMTPAdapter {
    public void sendEmail(GeodesyMailMessage message);
}
