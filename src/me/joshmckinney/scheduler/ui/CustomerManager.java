package me.joshmckinney.scheduler.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import me.joshmckinney.scheduler.App;
import me.joshmckinney.scheduler.dao.CustomerDao;
import me.joshmckinney.scheduler.model.Customer;
import me.joshmckinney.scheduler.utils.Database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class CustomerManager {

    @FXML Label custManagerHeader = null;
    @FXML TextField tfCustName;
    @FXML TextField tfCustAddress;
    @FXML TextField tfCustPhone;
    private Customer customer;
    private int id;
    private String name;
    private String address;
    private String phone;
    private String intent;
    private Connection conn;

    public void setIntent(String intent) {
        if(intent == "new") {
            this.intent = "new";
            custManagerHeader.setText("Create New Customer");
            name = tfCustName.getText();
            address = tfCustAddress.getText();
            phone = tfCustPhone.getText();
        } else {
            this.intent = "edit";
            custManagerHeader.setText("Edit Existing Customer");
            tfCustName.setText(name);
            tfCustAddress.setText(address);
            tfCustPhone.setText(phone);
        }
    }
    public void popCustomer(Customer customer) { this.customer = customer;}
    public void popId(int id) { this.id = id; }
    public void popName(String name) { this.name = name; }
    public void popAddress(String address) { this.address = address; }
    public void popPhone(String phone) { this.phone = phone; }

    @FXML private void saveButtonClick(ActionEvent event) throws SQLException {
        Database.connect();
        conn = Database.getConn();
        if(App.isConnected) {
            if(intent == "edit") {
                // Catch null values -- Rubric Exception Control F3
                try {
                    name = tfCustName.getText();
                    address = tfCustAddress.getText();
                    phone = tfCustPhone.getText();
                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                }
                // Validate data
                if(name.isEmpty()) {
                    infoAlert("Name is missing or invalid, please try again.");
                } else if (address.isEmpty()) {
                    infoAlert("Address is missing or invalid, please try again.");
                } else if (phone.isEmpty() || !phone.matches("^[0-9]+$") || phone.length() != 10) {
                    infoAlert("Phone number is missing or invalid, please try again.\n" +
                            "10 digits only, no symbols or formatting.");
                } else {
                    // Create the customer if all are validated
                    CustomerDao.updateCustomer(customer.getCustId(), name, address, phone);
                    Stage custManager = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    custManager.close();
                    CustomerDao.refreshCustomers();
                }
            } else {
                // Catch null values -- Rubric Exception Control F3
                try {
                    name = tfCustName.getText();
                    address = tfCustAddress.getText();
                    phone = tfCustPhone.getText();
                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                }
                // Validate data
                if(name.isEmpty()) {
                    infoAlert("Name is missing or invalid, please try again.");
                } else if (address.isEmpty()) {
                    infoAlert("Address is missing or invalid, please try again.");
                } else if (phone.isEmpty() || !phone.matches("^[0-9]+$") || phone.length() != 10) {
                    infoAlert("Phone number is missing or invalid, please try again.\n" +
                            "10 digits only, no symbols or formatting.");
                } else {
                    // Create the customer if all are validated
                    CustomerDao.createCustomer(name,address,phone);
                    Stage custManager = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    custManager.close();
                    CustomerDao.refreshCustomers();
                }
            }
        } else {
            Database.connectionError();
        }
        conn.close();
    }
    @FXML private void cancelButtonClick(ActionEvent event) {
        Stage custManager = (Stage) ((Node) event.getSource()).getScene().getWindow();
        custManager.close();
    }

    // Create alert dialogs for invalid info
    private void infoAlert(String string) {
        VBox v = new VBox();
        v.setAlignment(Pos.CENTER);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Invalid Customer");
        alert.setContentText(string);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            alert.close();
        }
    }
}
