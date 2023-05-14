package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Main extends Application
{
    private static final Logger logger = LogManager.getLogger(Main.class);
    @Override
    public void start(Stage stage) throws IOException
    {
        logger.info("Application started");

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
        stage.setTitle("EmailClient");
        stage.setScene(new Scene(root));
        stage.show();

        logger.info("/fxml/Login loaded successfully");
    }
    public static void main(String[] args)
    {
        launch(args);
    }
}
