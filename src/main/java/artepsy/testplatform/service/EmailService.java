package artepsy.testplatform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import org.springframework.core.io.ByteArrayResource;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendTestResultsEmail(String toEmail, String clientName, byte[] pdfContent) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(fromEmail);
        helper.setTo(toEmail);
        helper.setSubject("Rezultate test CRS - " + clientName);
        helper.setText("Bună ziua,\n\nVă mulțumim pentru participarea la testul CRS. Atașat găsiți rezultatele testului.\n\nCu stimă,\nEchipa ArtePsy");

        // Create a ByteArrayResource from the PDF content
        ByteArrayResource resource = new ByteArrayResource(pdfContent);
        helper.addAttachment("Rezultate_CRS.pdf", resource);

        emailSender.send(message);
    }
} 