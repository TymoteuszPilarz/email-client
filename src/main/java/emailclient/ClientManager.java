package emailclient;

import emailclient.providers.WPClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientManager
{
    private static final Logger logger = LogManager.getLogger(ClientManager.class);
    private static Client client;
    public static void login(String emailAddress, String password) throws LoginFailureException
    {
        logger.info("Logging in...");

        switch (emailAddress.substring(emailAddress.indexOf("@") + 1))
        {
            case "wp.pl":
                logger.info("provider wp.pl selected");

                client = new WPClient(emailAddress, password);

                break;
            case "interia.pl":
                //TODO
                break;
            case "o2.pl":
                //TODO
                break;
            case "gmail.com":
                //TODO
                break;
        }
        try
        {
            client.getEmails();

            logger.info("Logged in succesfully");
        }
        catch (Exception exception)
        {
            logger.error("Failed to log in");

            throw new LoginFailureException("Wrong email or password");
        }
    }

    public static void logout()
    {
        logger.info("Logging out...");

        client = null;
    }

    public static Client getClient()
    {
        logger.info("Getting client...");

        if (isLoggedIn())
        {
            logger.info("Client returned");

            return client;
        }
        else
        {
            logger.error("Failed to get client");

            throw new RuntimeException("login() must be used before getClient()");
        }
    }

    public static boolean isLoggedIn()
    {
        return (client != null ? true : false);
    }

}
