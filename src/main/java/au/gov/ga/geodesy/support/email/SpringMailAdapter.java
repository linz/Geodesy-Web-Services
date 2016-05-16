package au.gov.ga.geodesy.support.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import au.gov.ga.geodesy.port.adapter.smtp.SMTPAdapter;

@Component
public class SpringMailAdapter implements SMTPAdapter {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendEmail(GeodesyMailMessage message) {
        SimpleMailMessage emailMessage = message.getMailMessage();
        String msg = "sendEmail [ templateMessage: " + emailMessage + "]";
        logger.debug(msg);

        mailSender.send(emailMessage);
    }
}
