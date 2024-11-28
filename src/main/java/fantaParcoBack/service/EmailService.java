package fantaParcoBack.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendHtmlEmail(String to, String subject, String htmlContent, String logoPath) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8"); // Specifica la codifica

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        // Aggiungi il logo come allegato e usa un CID per referirlo nell'HTML
        helper.addInline("logo", new ClassPathResource(logoPath));

        // Configura il mittente con nome personalizzato e codifica UTF-8
        helper.setFrom(fromEmail, "FantaParco Dei Principi");

        emailSender.send(message);
    }
}