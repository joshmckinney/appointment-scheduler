package me.joshmckinney.scheduler.utils;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import me.joshmckinney.scheduler.App;

import java.sql.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class Database {

    public static Connection getConn() { return conn; }
    private static Connection conn = null;
    private static ResourceBundle language;
    private static String result;
    private Statement stmt = null;

    public static void connect() {

        // Fill in your own database connection details here
        final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
        final String DB_URL = "jdbc:mariadb://127.0.0.1/test";
        final String DBUSER = "user";
        final String DBPASS = "password";

        boolean res = false;

        try {
            Class.forName("org.mariadb.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, DBUSER, DBPASS);
            App.isConnected = true;
        } catch (ClassNotFoundException ex) {
            App.isConnected = false;
        }  catch (SQLException ex) {
            App.isConnected = false;
        }
    }

    public static void connectionError() {
        language = ResourceBundle.getBundle("me/joshmckinney/scheduler/Language", Locale.getDefault());
        System.out.println("No Connection");
        VBox v = new VBox();
        Scene notFoundScene = new Scene(v);
        Stage notFoundStage = new Stage();
        v.setAlignment(Pos.CENTER);
        Alert alert = new Alert(Alert.AlertType.WARNING);
        notFoundStage.setScene(notFoundScene);
        alert.initOwner(App.appWindow);
        alert.setTitle(language.getString("commT"));
        alert.setHeaderText(language.getString("commH"));
        alert.setContentText(language.getString("commC"));
        alert.showAndWait();
    }
}
