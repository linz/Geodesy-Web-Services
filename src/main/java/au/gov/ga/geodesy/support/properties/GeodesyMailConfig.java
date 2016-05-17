package au.gov.ga.geodesy.support.properties;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@PropertySource("classpath:email.properties")
public class GeodesyMailConfig {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${email.host}")
    private String emailHost;
    @Value("${email.port}")
    private Integer emailPort;
    @Value("${email.username}")
    private String username;
    @Value("${email.pass}")
    private String password;
    @Value("${mail.transport.protocol}")
    private String transportProtocol;
    @Value("${mail.smtp.auth:true}")
    private Boolean smtpAuth;
    @Value("${mail.smtp.starttls.enable:true}")
    private Boolean starttlsEnabled;
    @Value("${mail.debug:false}")
    private Boolean mailDebug;

    @Bean
    public JavaMailSenderImpl emailSender() {
        JavaMailSenderImpl emailSender = new JavaMailSenderImpl();
        emailSender.setHost(emailHost);
        emailSender.setPort(emailPort);
        emailSender.setUsername(username);
        emailSender.setPassword(password);
        // emailSender.setDefaultEncoding("UTF_8");
        Properties mailProps = new Properties();
        logger.debug(String.format("emailHost: %s, port: %s, username: %s, pw: %s", emailHost, emailPort,
                username, password));
        logger.debug(String.format("mail.transport: %s, mail.smtp.auth: %s, starttls.enable: %s, mail.debug: %s",
                transportProtocol, smtpAuth, starttlsEnabled, mailDebug));

        mailProps.setProperty("mail.transport.protocol", transportProtocol);
        mailProps.setProperty("mail.smtp.auth", Boolean.toString(smtpAuth));
        mailProps.setProperty("mail.smtp.starttls.enable", Boolean.toString(starttlsEnabled));
        mailProps.setProperty("mail.debug", Boolean.toString(mailDebug));
        emailSender.setJavaMailProperties(mailProps);
        return emailSender;
    }
}
