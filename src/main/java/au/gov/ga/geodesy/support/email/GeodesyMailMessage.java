package au.gov.ga.geodesy.support.email;

import org.springframework.mail.SimpleMailMessage;

public class GeodesyMailMessage {
    private final SimpleMailMessage message = new SimpleMailMessage();

    /**
     * 
     * @param from
     * @param subject
     * @param to
     *            - var args - separate to addresses with , (comma)
     */
    public GeodesyMailMessage(final String from, final String subject, final String... to) {
        message.setFrom(from);
        message.setSubject(subject);
        message.setTo(to);
    }

    public GeodesyMailMessage setReply(final String replyTo) {
        message.setReplyTo(replyTo);
        return this;
    }

    public GeodesyMailMessage setTo(final String to) {
        message.setTo(to);
        return this;
    }

    public GeodesyMailMessage setCc(final String cc) {
        message.setCc(cc);
        return this;
    }

    public GeodesyMailMessage setBcc(final String bcc) {
        message.setBcc(bcc);
        return this;
    }

    public GeodesyMailMessage setSubject(final String subject) {
        message.setSubject(subject);
        return this;
    }

    public GeodesyMailMessage setBody(final String body) {
        message.setText(body);
        return this;
    }
    
    public SimpleMailMessage getMailMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "GeodesyMailMessage [message=" + message + "]";
    }
    
    
}
