package pl.lssystems.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.lssystems.model.Email;

import javax.annotation.PostConstruct;
import javax.mail.*;
import java.io.IOException;
import java.util.*;

@Service
public class EmailService {

    @Value("${email.address}")
    String username;
    @Value("${email.password}")
    String password;
    @Value("${email.server}")
    String server;
    @Value("${email.port}")
    String port;
    @Value("${email.protocol}")
    String protocol;
    @Value("${email.proxy.host}")
    String proxyHost;
    @Value("${email.proxy.port}")
    String proxyPort;
    @Value("${email.folder}")
    String folder;
    @Value("${email.fetch.size}")
    int fetchSize;

    public List<Email> getLatestEmails() throws MessagingException, IOException {
        return readEmails();
    }

    public List<Email> getLatestEmailsForEmail(String email) throws MessagingException, IOException {
        List<Email> emails = readEmails();
        emails.removeIf(e -> !e.getTo().contains(email));
        return emails;
    }

    Properties props = new Properties();

    @PostConstruct
    void setup() {
        props.setProperty("mail.imap.host", server);
        props.setProperty("mail.imap.port", port);
        props.setProperty("mail.imap.user", username);
        props.setProperty("mail.store.protocol", protocol);
        props.setProperty("mail.imaps.proxy.host", proxyHost);
        props.setProperty("mail.imaps.proxy.port", proxyPort);
//        props.setProperty("mail.imaps.proxy.user", proxyUser);
//        props.setProperty("mail.imaps.proxy.password", proxyPassword);
    }

    private List<Email> readEmails() throws MessagingException, IOException {
        List<Email> emails = new ArrayList<>();
        Session session = Session.getDefaultInstance(props);
        Store store = session.getStore(protocol);
        store.connect(server, username, password);

        Folder emailFolder = store.getFolder(folder);
        emailFolder.open(Folder.READ_ONLY);

        int messageCount = emailFolder.getMessageCount();
        int fetchSizeInt = messageCount > fetchSize ? fetchSize : messageCount;
        if (fetchSizeInt > 0) {
            Message[] messages = emailFolder.getMessages(messageCount - fetchSizeInt + 1, messageCount);
            for (Message message : messages)
                emails.add(new Email(message));
        }

        Collections.reverse(emails);

        emailFolder.close(false);
        store.close();
        return emails;
    }

}
