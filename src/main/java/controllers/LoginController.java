package controllers;

import emailclient.ClientManager;
import emailclient.LoginFailureException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class LoginController
{
    private static final Logger logger = LogManager.getLogger(LoginController.class);
    @FXML
    private Button signInButton;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Text userMessageText;

    public void signInPressed(ActionEvent actionEvent)
    {
        logger.info("Sign In pressed");

        signInButton.setDisable(true);
        emailField.setDisable(true);
        passwordField.setDisable(true);

        userMessageText.setFill(Color.GREEN);
        userMessageText.setText("Logging in...");
        userMessageText.setVisible(true);

        new Thread(() ->
        {
            try
            {
                ClientManager.login(emailField.getText(), passwordField.getText());

                logger.info("Login succesful");

                Platform.runLater(() ->
                {
                    userMessageText.setFill(Color.GREEN);
                    userMessageText.setText("Login succesful");
                    Scene scene = signInButton.getScene();
                    try
                    {
                        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Mailbox.fxml"));
                        scene.getWindow().setWidth(1000);
                        scene.getWindow().setHeight(700);
                        scene.getWindow().centerOnScreen();
                        scene.setRoot(root);

                        logger.info("/fxml/Mailbox.fxml succesfully loaded");
                    }
                    catch (IOException ioException)
                    {
                        logger.error("Could not load /fxml/Mailbox.fxml");

                        throw new RuntimeException("Could not load /fxml/Mailbox.fxml");
                    }
                });
            }
            catch (LoginFailureException exception)
            {
                logger.error("Login failed");
                signInButton.setDisable(false);
                emailField.setDisable(false);
                passwordField.setDisable(false);

                Platform.runLater(() ->
                {
                    userMessageText.setFill(Color.RED);
                    userMessageText.setText("Wrong credentials");
                });
            }
        }).start();
    }

    public void fieldEdited(KeyEvent keyEvent)
    {
        userMessageText.setVisible(false);
    }
}
