package pl.lssystems.model;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Email {
    private final String from;
    private final String to;
    private final Instant receivedDate;
    private final String subject;
    private final String body;

    public Email(Message message) throws MessagingException, IOException {
        this.from = Arrays.stream(message.getFrom()).map(Address::toString).collect(Collectors.joining(","));
        this.to = Arrays.stream(message.getAllRecipients()).map(Address::toString).collect(Collectors.joining(","));
        this.receivedDate = message.getReceivedDate().toInstant();
        this.subject = message.getSubject();
        this.body = message.isMimeType("text/plain") || message.isMimeType("text/html") ?
                message.getContent().toString() :
                getTextFromMimeMultipart((MimeMultipart) message.getContent());
    }

    private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < mimeMultipart.getCount(); i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain") || bodyPart.isMimeType("text/html")) {
                content.append("\n").append(bodyPart.getContent());
            } else if (bodyPart.getContent() instanceof MimeMultipart){
                content.append(getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent()));
            }
        }
        return content.toString();
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public Instant getReceivedDate() {
        return receivedDate;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

}
