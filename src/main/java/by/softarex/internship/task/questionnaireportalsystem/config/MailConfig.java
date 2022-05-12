package by.softarex.internship.task.questionnaireportalsystem.config;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Setter
@ConfigurationProperties(prefix = "mail")
@Configuration
public class MailConfig {
    private String host;
    private Integer port;
    private String username;
    private String password;
    private String protocol;
    private Boolean debug;

    /* почему не вынести в @ConfigurationProperties эти проперти */
    //про такую штуку не знал

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", protocol);
        props.put("mail.debug", debug);

        return mailSender;
    }
}
