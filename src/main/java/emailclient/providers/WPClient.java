package emailclient.providers;

import emailclient.Client;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

public final class WPClient extends Client
{
    private final String emailAddress;
    private final Session smtpSession;
    private final Session imapSession;

    public WPClient(String emailAddress, String password)
    {
        this.emailAddress = emailAddress;

        Authenticator auth = new Authenticator()
        {
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(emailAddress, password);
            }
        };

        Properties smtpProps = new Properties();
        smtpProps.put("mail.smtp.host", "smtp.wp.pl");
        smtpProps.put("mail.smtp.ssl.enable", "true");
        smtpProps.put("mail.smtp.auth", "true");
        smtpProps.put("mail.smtp.port", "465");

        smtpSession = Session.getInstance(smtpProps, auth);

        Properties pop3Props = new Properties();
        pop3Props.put("mail.imap.host", "imap.wp.pl");
        pop3Props.put("mail.imap.ssl.enable", "true");
        pop3Props.put("mail.imap.auth", "true");
        pop3Props.put("mail.imap.port", "993");
        pop3Props.put("mail.store.protocol", "imap");

        imapSession = Session.getInstance(pop3Props, auth);
    }

    public void sendEmail(String recipient, String subject, String body) throws MessagingException
    {
        super.sendEmail(smtpSession, emailAddress, recipient, subject, body);
    }

    public emailclient.Message[] getEmails() throws MessagingException
    {
        return super.getEmails(imapSession);
    }
}
