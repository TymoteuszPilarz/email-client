package emailclient;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.*;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;

public abstract class Client
{
    private static final Logger logger = LogManager.getLogger(Client.class);
    public abstract void sendEmail(String recipient, String subject, String body) throws MessagingException;
    public void sendEmail(Session session, String sender, String recipient, String subject, String body) throws MessagingException
    {
        logger.info("Sending an email...");

        MimeMessage msg = new MimeMessage(session);

        msg.setFrom(sender);
        msg.setSubject(subject, "UTF-8");
        msg.setText(body, "UTF-8");
        msg.setSentDate(new Date());
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient, false));

        Transport.send(msg);
    }

    public abstract emailclient.Message[] getEmails() throws MessagingException;
    public emailclient.Message[] getEmails(Session session) throws MessagingException
    {
        logger.info("Fetching emails...");

        Store store = session.getStore();
        store.connect();

        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);

        Message[] messages = inbox.getMessages();
        emailclient.Message ret[] = new emailclient.Message[messages.length];

        for (int i = 0; i < ret.length; i++)
        {
            ret[i] = new emailclient.Message(messages[i]);
        }

        inbox.close(false);
        store.close();

        logger.info("Emails fetched successfully");

        return ret;
    }
}
