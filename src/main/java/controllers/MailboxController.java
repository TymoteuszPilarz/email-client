package controllers;

import emailclient.ClientManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.MessagingException;
import java.io.IOException;

public class MailboxController
{
    private static final Logger logger = LogManager.getLogger(MailboxController.class);
    private Stage composeWindow;
    private customcontrols.Message latestSelectedMessage;
    @FXML
    Label messageCount;
    @FXML
    Label subject;
    @FXML
    TextArea body;
    @FXML
    VBox messageList;

    public void refreshPressed(ActionEvent actionEvent)
    {
        logger.info("Refresh button pressed");

        loadMessages();
    }
    public void composePressed(ActionEvent actionEvent)
    {
        logger.info("Compose button pressed");

        try
        {
            if (composeWindow != null)
                composeWindow.close();

            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Compose.fxml"));
            composeWindow = new Stage();
            composeWindow.setTitle("Compose new email");
            composeWindow.setAlwaysOnTop(true);
            composeWindow.setScene(new Scene(root));
            composeWindow.show();

            logger.info("/fxml/Compose loaded succesfully");
        }
        catch (IOException ioException)
        {
            logger.error("Could not load /fxml/Compose");
            throw new RuntimeException("Could not load /fxml/Compose.fxml");
        }
    }
    public void logoutPressed(ActionEvent actionEvent)
    {
        logger.info("Logout button pressed");

        ClientManager.logout();
        Scene scene = messageCount.getScene();
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
            scene.getWindow().setWidth(325);
            scene.getWindow().setHeight(200);
            scene.getWindow().centerOnScreen();
            scene.setRoot(root);

            logger.info("/fxml/Login.fxml succesfully loaded");
        }
        catch (IOException ioException)
        {
            logger.error("Could not load /fxml/Login.fxml");

            throw new RuntimeException("Could not load /fxml/Login.fxml");
        }
    }
    public void messagePressed(MouseEvent mouseEvent)
    {
        logger.info("Email message field pressed");

        Node node = (Node) mouseEvent.getTarget();
        while (node.getClass() != customcontrols.Message.class)
        {
            node = node.getParent();
        }

        customcontrols.Message message_control = (customcontrols.Message) node;

        latestSelectedMessage.setBorder(null);
        latestSelectedMessage = message_control;
        message_control.setBorder(new Border(new BorderStroke(Color.GRAY,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        emailclient.Message message = message_control.getMessage();

        subject.setText(message.getSubject());
        body.setText(message.getBody());
    }

    @FXML
    private void initialize()
    {
        logger.info("Mailbox initialization started");

        loadMessages();
    }

    private void loadMessages()
    {
        logger.info("Loading messages...");

        messageList.getChildren().clear();
        try
        {
            emailclient.Message[] messages = ClientManager.getClient().getEmails();
            messageCount.setText(messages.length + " messages");

            if (messages.length == 0)
            {
                logger.warn("No messages");

                subject.setText("");
            }
            else
            {
                subject.setText(messages[messages.length - 1].getSubject());
                body.setText(messages[messages.length - 1].getBody());

                for (int i = messages.length - 1; i >= 0; i--)
                {
                    customcontrols.Message message = new customcontrols.Message(messages[i]);
                    message.addEventFilter(MouseEvent.MOUSE_CLICKED, this::messagePressed);
                    messageList.getChildren().add(message);
                }

                latestSelectedMessage = ( customcontrols.Message) messageList.getChildren().get(0);
                ((customcontrols.Message) messageList.getChildren().get(0)).setBorder(new Border(new BorderStroke(Color.GRAY,
                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            }

            logger.info("Messages succesfully loaded");
        }
        catch (MessagingException messagingException)
        {
            logger.error("Failure while loading messages");

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(composeWindow);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load messages");
            alert.setContentText("Check your internet connection");

            alert.showAndWait();
        }
    }
}
