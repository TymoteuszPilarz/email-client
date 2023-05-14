package customcontrols;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Message extends HBox
{
    private static final Logger logger = LogManager.getLogger(Message.class);
    private emailclient.Message message;
    @FXML
    private Label address;
    @FXML
    private Label subject;
    @FXML
    private Label body;
    @FXML
    private Label date;

    public Message(emailclient.Message message)
    {
        logger.info("Constructing Message custom control...");

        this.message = message;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Message.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try
        {
            fxmlLoader.load();

            logger.info("/fxml/Message loaded succesfully");
        }
        catch (IOException exception)
        {
            logger.error("Could not load /fxml/Message");

            throw new RuntimeException(exception);
        }
    }

    public emailclient.Message getMessage()
    {
        return message;
    }

    @FXML
    private void initialize()
    {
        logger.info("Message custmom control initialization started");

        address.setText(message.getFrom());
        subject.setText(message.getSubject());
        body.setText(message.getBody().split("\n")[0].trim());
        date.setText(message.getReceivedDate());
    }
}
