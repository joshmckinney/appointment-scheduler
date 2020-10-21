package me.joshmckinney.scheduler.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import me.joshmckinney.scheduler.App;
import me.joshmckinney.scheduler.utils.Database;
import me.joshmckinney.scheduler.utils.Log;

import java.io.IOException;
import java.sql.*;
import java.util.Locale;
import java.util.ResourceBundle;


public class Login {

    private ResourceBundle language;
    @FXML private AnchorPane loginPane;
    @FXML private TextField tfUser;
    @FXML private PasswordField pfPass;
    @FXML private Button loginButton;
    private int logUserId;
    private Connection conn;

    public void login() throws IOException {
        // Get login fields
        String username = tfUser.getText();
        String password = pfPass.getText();
        // Match user and pass SQL statement
        String statement = "SELECT * FROM user WHERE user.userName = '"
                + username + "' AND user.password = '" + password + "';";
        // Execute query and process result [Valid || NULL]
        int userId = 0;
        String userName = null;
        ResultSet rs;
            try {
                Statement stmt = conn.createStatement();
                rs = stmt.executeQuery(statement);
                while (rs.next()) {
                userId = rs.getInt(1);
                userName = rs.getString(2);
                }
                logUserId = userId;
            } catch (Exception e) {

            }
        // Validate username and password match -- Rubric Exception Control F4
        if(userId == 0) {
            VBox v = new VBox();
            Scene notFoundScene = new Scene(v);
            Stage notFoundStage = new Stage();
            v.setAlignment(Pos.CENTER);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            notFoundStage.setScene(notFoundScene);
            alert.initOwner(App.appWindow);
            alert.setTitle(language.getString("loginT"));
            alert.setHeaderText(language.getString("loginH"));
            alert.setContentText(language.getString("loginC"));
            alert.showAndWait();
        } else {
            // Query returned successful, user authed
            System.out.println("User #" + userId + " logged in.");
            Log.login(logUserId);
            Main.setSessionUserId(userId);
            Main.setSessionUserName(userName);
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/me/joshmckinney/scheduler/ui/fxml/main.fxml"));
            Parent root = loader.load();
            Scene mainScene = new Scene(root);
            Stage mainStage = new Stage();
            mainStage.setScene((mainScene));
            mainStage.setTitle("Scheduler");
            mainStage.show();
            App.appStage.close();
            App.appStage = mainStage;
        }
    }

    @FXML public void loginButton() throws IOException, SQLException {
        Database.connect();
        conn = Database.getConn();
        if(App.isConnected) {
            login();
            conn.close();
        } else {
            Database.connectionError();
        }
    }

    @FXML public void loginReturnKey(KeyEvent event) throws IOException, SQLException {
        if(event.getCode() == KeyCode.ENTER) {
            Database.connect();
            conn = Database.getConn();
            if(App.isConnected) {
                login();
                conn.close();
            } else {
                Database.connectionError();
            }
        }
    }

    @FXML public void clickFocusPane() {
        loginPane.requestFocus();
    }

    public void initialize() throws IOException {
        language = ResourceBundle.getBundle("me/joshmckinney/scheduler/Language", Locale.getDefault());
        tfUser.setPromptText(language.getString("username"));
        pfPass.setPromptText(language.getString("password"));
        loginButton.setText(language.getString("login"));
        System.out.println("Locale set to: " + Locale.getDefault());
    }
}
