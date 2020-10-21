package me.joshmckinney.scheduler;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

//------------------------------//
//      Appointment Scheduler   //
//------------------------------//
// Developed by Joshua McKinney //
//------------------------------//

public class App extends Application {

    public static Window appWindow;
    public static Stage appStage;
    public static boolean isConnected;

    @Override
    public void start(Stage loginStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/me/joshmckinney/scheduler/ui/fxml/login.fxml"));
        loginStage.setTitle("Scheduler Login");
        loginStage.setScene(new Scene(root, 300, 300));
        loginStage.show();
        loginStage.requestFocus();
        this.appStage = loginStage;
        this.appWindow = loginStage.getScene().getWindow();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
