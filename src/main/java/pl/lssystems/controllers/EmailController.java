package pl.lssystems.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.lssystems.model.Email;
import pl.lssystems.services.EmailService;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

@RestController
public class EmailController {

    private final EmailService emailService;

    EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/emails")
    List<Email> all() throws MessagingException, IOException {
        return emailService.getLatestEmails();
    }

    @GetMapping("/emails/{email}")
    List<Email> forEmail(@PathVariable String email) throws MessagingException, IOException {
        return emailService.getLatestEmailsForEmail(email);
    }

}
