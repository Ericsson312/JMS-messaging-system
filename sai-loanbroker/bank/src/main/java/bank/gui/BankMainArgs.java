package bank.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

public class BankMainArgs extends Application {

    private static String queueName = null;
    private static String bankName = null;

    public static void main(String[] args) {
        if (args.length < 2 ){
            throw new IllegalArgumentException("Arguments are missing. You must provide two arguments: BANK_REQUEST_QUEUE and BANK_NAME");
        }
        if (args[0] == null){
            throw new IllegalArgumentException("Please provide BANK_REQUEST_QUEUE.");
        }
        if (args[1] == null){
            throw new IllegalArgumentException("Please provide BANK_NAME.");
        }
        queueName = args[0];
        bankName =args[1];
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        final Logger logger = LoggerFactory.getLogger(getClass());
        primaryStage.setOnCloseRequest(new EventHandler<>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });

        URL url  = getClass().getClassLoader().getResource( "bank.fxml" );
        if (url != null) {

            logger.info("Starting "+bankName+" bank on " + queueName + ".");

            FXMLLoader loader = new FXMLLoader(url);

            // Create a controller instance and set it in FXMLLoader
            BankController controller = new BankController(queueName, bankName);
            loader.setController(controller);

            Parent root = loader.load();
            primaryStage.setTitle("BANK - " + bankName);

            primaryStage.setOnCloseRequest(new EventHandler<>() {
                @Override
                public void handle(WindowEvent t) {
                    Platform.exit();
                    System.exit(0);
                }
            });

            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/bank.png")));
            primaryStage.setScene(new Scene(root, 500, 300));
            primaryStage.show();
        }
        else {
            logger.error("Could not load frame from bank.fxml.");
        }
    }
}
