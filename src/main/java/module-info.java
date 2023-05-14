module com.emailclient.emailclient
{
    requires javafx.controls;
    requires javafx.fxml;
    requires java.mail;
    requires org.jsoup;
    requires org.apache.logging.log4j;

    opens main to javafx.fxml;
    opens controllers to javafx.fxml;
    opens customcontrols to javafx.fxml;
    exports main;
}