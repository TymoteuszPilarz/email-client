package emailclient;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Message
{
    private static final Logger logger = LogManager.getLogger(Message.class);
    private String from;
    private String to;
    private String subject;
    private String body;
    private String receivedDate;

    public Message(javax.mail.Message message)
    {
        logger.info("Constructing Message...");

        try
        {
            to = ((InternetAddress) message.getAllRecipients()[0]).getAddress();
            logger.info("Recipient loaded succesfully");
        }
        catch (Exception exception)
        {
            logger.warn("Failed to load recipient");

            to = "Unknown";
        }

        try
        {
            from = ((InternetAddress) message.getFrom()[0]).getAddress();

            logger.info("Sender loaded succesfully");
        }
        catch (Exception exception)
        {
            logger.warn("Failed to load sender");

            from = "Unknown";
        }

        try
        {
            subject = message.getSubject().trim();

            logger.info("Subject loaded succesfully");
        }
        catch (MessagingException messagingException)
        {
            logger.warn("Failed to load subject");

            subject = "No subject";
        }

        try
        {
            body = getTextFromMessage(message).trim();

            logger.info("Body loaded succesfully");
        }
        catch (Exception messagingException)
        {
            logger.warn("Failed to load body");

            body = "";
        }

        try
        {
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String strDate = dateFormat.format(message.getReceivedDate());
            receivedDate = strDate;

            logger.info("Date loaded succesfully");
        }
        catch (MessagingException messagingException)
        {
            logger.warn("Failed to load date");

            receivedDate = "Unknown";
        }
    }

    public Message(String from, String to, String subject, String message)
    {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.body = message;
    }

    public void setFrom(String from)
    {
        this.from = from;
    }
    public void setTo(String to)
    {
        this.to = to;
    }
    public void setSubject(String subject)
    {
        this.subject = subject;
    }
    public void setBody(String message)
    {
        this.body = message;
    }

    public String getFrom()
    {
        return from;
    }
    public String getTo()
    {
        return to;
    }
    public String getSubject()
    {
        return subject;
    }
    public String getBody()
    {
        return body;
    }
    public String getReceivedDate()
    {
        return receivedDate;
    }

    private String getTextFromMessage(javax.mail.Message message) throws MessagingException, IOException
    {
        if (message.isMimeType("text/plain"))
        {
            return message.getContent().toString();
        }
        if (message.isMimeType("multipart/*"))
        {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            return getTextFromMimeMultipart(mimeMultipart);
        }
        return "";
    }
    private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException
    {
        String result = "";
        for (int i = 0; i < mimeMultipart.getCount(); i++)
        {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain"))
            {
                return result + "\n" + bodyPart.getContent();
            }
            result += this.parseBodyPart(bodyPart);
        }
        return result;
    }
    private String parseBodyPart(BodyPart bodyPart) throws MessagingException, IOException
    {
        if (bodyPart.isMimeType("text/html"))
        {
            return "\n" + org.jsoup.Jsoup.parse(bodyPart.getContent().toString()).text();
        }
        if (bodyPart.getContent() instanceof MimeMultipart)
        {
            return getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
        }

        return "";
    }
}
