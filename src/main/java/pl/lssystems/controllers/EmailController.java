package pl.lssystems.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
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

    @GetMapping("/test/{target}/{mode}")
    public ResponseEntity<String> test(@PathVariable String target, @PathVariable String mode) throws MessagingException, IOException {
        System.out.println("Controller test: " + target + " | " + mode);

        if (target.equals("ws")) {
            ResponseEntity.ok().body("OK");
        }
        if (target.equals("xxx")) {
            return ResponseEntity.badRequest().body("Unknown target: " + target + "; Available targets: " + "ws, adapter");
        }
        if ( target.equals("error")) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Mock error response");
        }
        return ResponseEntity.ok("OK");
    }

}
