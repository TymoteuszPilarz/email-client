package controllers;

import emailclient.ClientManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.MessagingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComposeController
{
    private static final Logger logger = LogManager.getLogger(ComposeController.class);
    @FXML
    private TextField to;
    @FXML
    private TextField subject;
    @FXML
    private TextArea body;

    public void sendPressed()
    {
        logger.info("Send button pressed");

        Stage composeWindow = (Stage) to.getScene().getWindow();

        String email = to.getText();
        Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        Matcher mat = pattern.matcher(email);

        if(!mat.matches())
        {
            logger.warn("Wrong email format");

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(composeWindow);
            alert.setTitle("Error");
            alert.setHeaderText("Incorrect email address");
            alert.setContentText("Email address must be typed in format: example@example.com");

            alert.showAndWait();

            return;
        }

        try
        {
            ClientManager.getClient().sendEmail(to.getText(), subject.getText(), body.getText());
            composeWindow.close();

            logger.info("Email sent succesfully");
        }
        catch (MessagingException messagingException)
        {
            logger.error("Failure while sending an email");

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(composeWindow);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to send an email");
            alert.setContentText("Check your internet connection");

            alert.showAndWait();
        }
    }
    public void cancelPressed()
    {
        logger.info("Cancel button pressed");

        Stage composeWindow = (Stage) to.getScene().getWindow();
        composeWindow.close();
    }
}
