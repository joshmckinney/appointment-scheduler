package me.joshmckinney.scheduler.ui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import me.joshmckinney.scheduler.App;
import me.joshmckinney.scheduler.dao.AppointmentDao;
import me.joshmckinney.scheduler.dao.CustomerDao;
import me.joshmckinney.scheduler.dao.UserDao;
import me.joshmckinney.scheduler.model.Appointment;
import me.joshmckinney.scheduler.model.Customer;
import me.joshmckinney.scheduler.model.User;
import me.joshmckinney.scheduler.utils.Database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class Main {

    // User ID and Name from login to store for session.
    private static int userId;
    private static String userName;
    // Table
    @FXML private TableView<Customer> custTable;
    @FXML private TableView<Appointment> apptTable;
    @FXML private TableColumn<Customer, String> colCustName;
    @FXML private TableColumn<Customer, String> colCustAddress;
    @FXML private TableColumn<Customer, String> colCustPhone;
    @FXML private TableColumn<Appointment, String> colConsultant;
    @FXML private TableColumn<Appointment, String> colCustomer;
    @FXML private TableColumn<Appointment, String> colType;
    @FXML private TableColumn<Appointment, Date> colDate;
    @FXML private TableColumn<Appointment, Time> colStartTime;
    @FXML private TableColumn<Appointment, Time> colEndTime;
    // Tabs
    @FXML private Tab apptTab;
    @FXML private Tab custTab;
    // Menu Items
    @FXML private MenuItem apptNew;
    @FXML private MenuItem custNew;
    @FXML private MenuItem mClickDelete;
    @FXML private RadioMenuItem mClickViewActive;
    @FXML private RadioMenuItem mClickViewInactive;
    // Search
    @FXML private TextField tfCustSearch;
    @FXML private TextField tfApptSearch;
    @FXML private Button searchApptClearButton;
    @FXML private Button searchCustClearButton;

    private Connection conn;

    // Set and get User ID
    public static void setSessionUserId(int id) {
        userId = id;
    }
    public static int getSessionUserId() { return userId; }
    // Set and get User Name
    public static void setSessionUserName(String name) { userName = name; }
    public static String getSessionUserName() { return userName; }

    // Menu Button - Reports Monthly Types
    @FXML private void mClickReportMonthlyType() throws SQLException {
        Database.connect();
        conn = Database.getConn();
        if(App.isConnected) {
            AppointmentDao.refreshAppointments();
            conn.close();
            VBox v = new VBox();
            v.setAlignment(Pos.CENTER);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Monthly Type Count Report");
            alert.setHeaderText("Appointments this month:");
            Report.typeReport();
            alert.setContentText(Report.getTypeReport());
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                alert.close();
            }
        } else {
            Database.connectionError();
        }
    }

    // Menu Button - Reports Weekly
    @FXML private void mClickReportByCons() throws IOException, SQLException {
        Database.connect();
        conn = Database.getConn();
        if(App.isConnected) {
            UserDao.refreshUsers();
            AppointmentDao.refreshAppointments();
            conn.close();
            ChoiceDialog<String> dialog = new ChoiceDialog<>(null, User.getNames());
            dialog.setTitle("Appointments by Consultant Report");
            dialog.setHeaderText("Consultant Needed");
            dialog.setContentText("Please choose a consultant:");
            FXMLLoader loader = new FXMLLoader(Report.class.getResource("/me/joshmckinney/scheduler/ui/fxml/report.fxml"));
            Parent root = loader.load();
            Report controller = loader.getController();
            Scene reportScene = new Scene(root);
            Stage reportStage = new Stage();
            reportStage.setScene((reportScene));
            reportStage.setTitle("Consultant Schedule Report");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()){
                controller.consReport(result.get());
                controller.setTable();
                reportStage.show();
            } else {
                dialog.close();
            }
        } else {
            Database.connectionError();
        }
    }

    // Menu Button - Reports Weekly
    @FXML private void mClickReportWeekly() throws SQLException {
        Database.connect();
        conn = Database.getConn();
        if(App.isConnected) {
            AppointmentDao.refreshAppointments();
            conn.close();
            VBox v = new VBox();
            v.setAlignment(Pos.CENTER);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Weekly Appointment Count Report");
            alert.setHeaderText("Number of appointments this week:");
            alert.setContentText(Integer.toString(Report.getWeeklyCount()));
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                alert.close();
            }
        } else {
            Database.connectionError();
        }
    }

    // Menu Button - About
    @FXML private void mClickAbout(ActionEvent event) {
        VBox v = new VBox();
        v.setAlignment(Pos.CENTER);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Application Scheduler\n\nDeveloped by Joshua McKinney");
        alert.setContentText("This is a scheduling application that allows you to create and manage appointments and customers. " +
                "Additional reporting services are also available. Thank you for trying!");
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            alert.close();
        }
    }

    // Menu Button - Exit
    @FXML private void mClickExit(ActionEvent event) {
        VBox v = new VBox();
        v.setAlignment(Pos.CENTER);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(App.appWindow);
        alert.setTitle("Exit");
        alert.setHeaderText("Are you sure you wish to exit?");
        alert.setContentText("You may lose unsaved data.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Platform.exit();
        }
    }
    @FXML private void mClickViewActive() {
        Database.connect();
        conn = Database.getConn();
        if(App.isConnected) {
            CustomerDao.refreshCustomers();
            custTable.setItems(Customer.getAllCustomers());
            mClickDelete.setText("Delete Selected");
            custTab.setText("Customers (Active)");
            custTable.refresh();
            custTable.getSortOrder().add(colCustName);
        } else {
            Database.connectionError();
        }
    }
    @FXML private void mClickViewInactive() {
        Database.connect();
        conn = Database.getConn();
        if(App.isConnected) {
            CustomerDao.refreshCustomers();
            custTable.setItems(Customer.getInactiveCustomers());
            mClickDelete.setText("Undelete Selected");
            custTab.setText("Customers (Inactive)");
            custTable.refresh();
            custTable.getSortOrder().add(colCustName);
        } else {
            Database.connectionError();
        }
    }
    @FXML private void mClickViewDaily() {
        Database.connect();
        conn = Database.getConn();
        if(App.isConnected) {
            AppointmentDao.refreshAppointments();
            apptTable.setItems(Appointment.getAppointmentsDaily(LocalDate.now()));
            apptTab.setText("Appointments (Daily)");
            apptTable.refresh();
            apptTable.getSortOrder().add(colDate);
            apptTable.getSortOrder().add(colStartTime);
        } else {
            Database.connectionError();
        }
    }
    @FXML private void mClickViewWeekly() {
        Database.connect();
        conn = Database.getConn();
        if(App.isConnected) {
            AppointmentDao.refreshAppointments();
            apptTable.setItems(Appointment.getAppointmentsWeekly());
            apptTab.setText("Appointments (Weekly)");
            apptTable.refresh();
            apptTable.getSortOrder().add(colDate);
            apptTable.getSortOrder().add(colStartTime);
        } else {
            Database.connectionError();
        }
    }
    @FXML private void mClickViewMonthly() {
        Database.connect();
        conn = Database.getConn();
        if(App.isConnected) {
            AppointmentDao.refreshAppointments();
            apptTable.setItems(Appointment.getAppointmentsMonthly());
            apptTab.setText("Appointments (Monthly)");
            apptTable.refresh();
            apptTable.getSortOrder().add(colDate);
            apptTable.getSortOrder().add(colStartTime);
        } else {
            Database.connectionError();
        }
    }
    @FXML private void mClickViewAll() {
        Database.connect();
        conn = Database.getConn();
        if(App.isConnected) {
            AppointmentDao.refreshAppointments();
            apptTable.setItems(Appointment.getAppointments());
            apptTab.setText("Appointments (All)");
            apptTable.refresh();
            apptTable.getSortOrder().add(colDate);
            apptTable.getSortOrder().add(colStartTime);
        } else {
            Database.connectionError();
        }
    }
    @FXML private void newApptAction() throws IOException, NullPointerException {
        try {
            Customer customer = custTable.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader(ApptManager.class.getResource("/me/joshmckinney/scheduler/ui/fxml/apptManager.fxml"));
            Parent root = loader.load();
            ApptManager controller = loader.getController();
            controller.setIntent("new");
            System.out.println(customer.getCustName());
            controller.popConsId(userId);
            controller.popConsName(userName);
            controller.popCustId(customer.getCustId());
            controller.popCustName(customer.getCustName());
            Scene mainScene = new Scene(root);
            Stage mainStage = new Stage();
            mainStage.setScene((mainScene));
            mainStage.setTitle("Manage Appointment");
            mainStage.show();
            // Use of lambda is cleaner and more efficient instead of defining and calling a new window event
            mainStage.setOnCloseRequest(event -> {
                apptTable.refresh();
                apptTable.getSortOrder().add(colDate);
                apptTable.getSortOrder().add(colStartTime);
            });
        } catch (NullPointerException ex) {
            System.out.println("No selection");
            VBox v = new VBox();
            v.setAlignment(Pos.CENTER);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(App.appWindow);
            alert.setTitle("Error");
            alert.setHeaderText("No customer selected.");
            alert.setContentText("Please make a valid selection and try again.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                alert.close();
            }
        }
    }
    @FXML private void newCustAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(CustomerManager.class.getResource("/me/joshmckinney/scheduler/ui/fxml/customerManager.fxml"));
        Parent root = loader.load();
        CustomerManager controller = loader.getController();
        controller.setIntent("new");
        Scene mainScene = new Scene(root);
        Stage mainStage = new Stage();
        mainStage.setScene((mainScene));
        mainStage.setTitle("Manage Customer");
        mainStage.show();
        // Use of lambda is cleaner and more efficient instead of defining and calling a new window event
        mainStage.setOnCloseRequest(event -> {
            custTable.refresh();
            custTable.getSortOrder().add(colCustName);
        });
    }

    @FXML private void editAction() throws IOException {
        if(apptTab.isSelected()) {
            if(apptTable.getSelectionModel().isEmpty()) {
                System.out.println("No selection");
                VBox v = new VBox();
                v.setAlignment(Pos.CENTER);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(App.appWindow);
                alert.setTitle("Error");
                alert.setHeaderText("No appointment selected.");
                alert.setContentText("Please make a valid selection and try again.");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    alert.close();
                }
            } else {
                Appointment appointment = apptTable.getSelectionModel().getSelectedItem();
                FXMLLoader loader = new FXMLLoader(ApptManager.class.getResource("/me/joshmckinney/scheduler/ui/fxml/apptManager.fxml"));
                Parent root = loader.load();
                ApptManager controller = loader.getController();
                controller.popAppointment(appointment);
                controller.popApptId(appointment.getApptId());
                controller.popConsId(appointment.getConsId());
                controller.popCustId(appointment.getCustId());
                controller.popConsName(appointment.getConsName());
                controller.popCustName(appointment.getCustName());
                controller.popType(appointment.getType());
                controller.popDate(appointment.getDate());
                controller.popStartTime(appointment.getStartTime());
                controller.popEndTime(appointment.getEndTime());
                controller.setIntent("edit");
                Scene mainScene = new Scene(root);
                Stage mainStage = new Stage();
                mainStage.setScene((mainScene));
                mainStage.setTitle("Manage Appointment");
                mainStage.show();
                // Use of lambda is cleaner and more efficient instead of defining and calling a new window event
                mainStage.setOnCloseRequest(event -> {
                    apptTable.refresh();
                    apptTable.getSortOrder().add(colDate);
                    apptTable.getSortOrder().add(colStartTime);
                });
            }
        } else {
            if(custTable.getSelectionModel().isEmpty()) {
                System.out.println("No selection");
                VBox v = new VBox();
                v.setAlignment(Pos.CENTER);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(App.appWindow);
                alert.setTitle("Error");
                alert.setHeaderText("No customer selected.");
                alert.setContentText("Please make a valid selection and try again.");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    alert.close();
                }
            } else {
                Customer customer = custTable.getSelectionModel().getSelectedItem();
                FXMLLoader loader = new FXMLLoader(CustomerManager.class.getResource("/me/joshmckinney/scheduler/ui/fxml/customerManager.fxml"));
                Parent root = loader.load();
                CustomerManager controller = loader.getController();
                controller.popCustomer(customer);
                controller.popId(customer.getCustId());
                controller.popName(customer.getCustName());
                controller.popAddress(customer.getCustAddress());
                controller.popPhone(customer.getCustPhone());
                controller.setIntent("edit");
                Scene mainScene = new Scene(root);
                Stage mainStage = new Stage();
                mainStage.setScene((mainScene));
                mainStage.setTitle("Manage Customer");
                mainStage.show();
                // Use of lambda is cleaner and more efficient instead of defining and calling a new window event
                mainStage.setOnCloseRequest(event -> {
                    custTable.refresh();
                    custTable.getSortOrder().add(colCustName);
                });
            }
        }
    }
    @FXML private void deleteAction() throws SQLException {
        Database.connect();
        conn = Database.getConn();
        if (App.isConnected) {
            if (apptTab.isSelected()) {
                if (apptTable.getSelectionModel().isEmpty()) {
                    System.out.println("No selection");
                    VBox v = new VBox();
                    v.setAlignment(Pos.CENTER);
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.initOwner(App.appWindow);
                    alert.setTitle("Error");
                    alert.setHeaderText("No appointment selected.");
                    alert.setContentText("Please make a valid selection and try again.");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        alert.close();
                    }
                } else {
                    VBox v = new VBox();
                    v.setAlignment(Pos.CENTER);
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.initOwner(App.appWindow);
                    alert.setTitle("Delete Appointment");
                    alert.setHeaderText("Are you sure you wish to delete this appointment?");
                    alert.setContentText(apptTable.getSelectionModel().getSelectedItem().getCustName() +
                            " " +
                            apptTable.getSelectionModel().getSelectedItem().getType() +
                            " at " +
                            apptTable.getSelectionModel().getSelectedItem().getStartTime() +
                            ", " +
                            apptTable.getSelectionModel().getSelectedItem().getEndTime() +
                            ", " +
                            apptTable.getSelectionModel().getSelectedItem().getDate());
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        AppointmentDao.deleteAppointment(apptTable.getSelectionModel().getSelectedItem().getApptId());
                        apptTable.refresh();
                        apptTable.getSortOrder().add(colDate);
                        apptTable.getSortOrder().add(colStartTime);
                        alert.close();
                    } else {
                        alert.close();
                    }
                }
            } else {
                if (custTable.getSelectionModel().isEmpty()) {
                    System.out.println("No selection");
                    VBox v = new VBox();
                    v.setAlignment(Pos.CENTER);
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.initOwner(App.appWindow);
                    alert.setTitle("Error");
                    alert.setHeaderText("No customer selected.");
                    alert.setContentText("Please make a valid selection and try again.");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        alert.close();
                    }
                } else {
                    if (custTable.getSelectionModel().getSelectedItem().getCustIsActive()) {
                        VBox v = new VBox();
                        v.setAlignment(Pos.CENTER);
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.initOwner(App.appWindow);
                        alert.setTitle("Delete Customer");
                        alert.setHeaderText("Are you sure you wish to to delete\n" +
                                custTable.getSelectionModel().getSelectedItem().getCustName() + "?");
                        alert.setContentText("All associated appointments will also be deleted!");
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == ButtonType.OK) {
                            int custId = custTable.getSelectionModel().getSelectedItem().getCustId();
                            CustomerDao.deleteCustomer(custId);
                            AppointmentDao.deleteAppointmentByCustId(custId);
                            custTable.refresh();
                            custTable.getSortOrder().add(colCustName);
                            apptTable.refresh();
                            apptTable.getSortOrder().add(colDate);
                            apptTable.getSortOrder().add(colStartTime);
                            alert.close();
                        }
                    } else {
                        CustomerDao.undeleteCustomer(custTable.getSelectionModel().getSelectedItem().getCustId());
                        if (custTable.getSelectionModel().isEmpty()) {
                            mClickViewActive();
                        }
                    }
                }
            }
            conn.close();
        }
    }

    @FXML private void apptTabClick() throws SQLException {
        if(apptTab.isSelected()) {
            apptNew.setDisable(true);
            mClickDelete.setText("Delete Selected");
            Database.connect();
            conn = Database.getConn();
            if(App.isConnected) {
                AppointmentDao.refreshAppointments();
                apptTable.getSortOrder().add(colDate);
                apptTable.refresh();
                conn.close();
            } else {
                Appointment.getAppointments().clear();
                apptTable.refresh();
                Database.connectionError();
            }
        }
    }

    @FXML private void custTabClick() throws SQLException {
        if(custTab.isSelected()) {
            apptNew.setDisable(false);
            Database.connect();
            conn = Database.getConn();
            if(mClickViewInactive.isSelected()) {
                mClickDelete.setText("Undelete Selected");
            } else {
                mClickDelete.setText("Delete Selected");
            }
            if(App.isConnected) {
                CustomerDao.refreshCustomers();
                custTable.getSortOrder().add(colCustName);
                custTable.refresh();
                conn.close();
            } else {
                Customer.getAllCustomers().clear();
                custTable.refresh();
                Database.connectionError();
            }
        }
    }

    @FXML private void searchAppt(KeyEvent event) throws IOException {
        if((Appointment.lookupAppointment(tfApptSearch.getText()).isEmpty()) && ((tfApptSearch.getText().isEmpty()))) {
            searchClearAppt();
        } else if(tfApptSearch.getText().isEmpty()) {
            clearHideAppt();
        } else {
            apptTable.setItems(Appointment.getLookupAppointment());
            apptTable.refresh();
            clearShowAppt();
        }
    }

    @FXML private void searchCust(KeyEvent event) throws IOException {
        if((Customer.lookupCustomer(tfCustSearch.getText()).isEmpty()) && ((tfCustSearch.getText().isEmpty()))) {
            searchClearCust();
        } else if(tfCustSearch.getText().isEmpty()) {
            clearHideCust();
        } else {
            custTable.setItems(Customer.getLookupCustomer());
            custTable.refresh();
            clearShowCust();
        }
    }

    @FXML private void searchClearAppt() {
        tfApptSearch.clear();
        apptTable.setItems(Appointment.getAppointments());
        apptTable.refresh();
        clearHideAppt();
    }

    @FXML private void searchClearCust() {
        tfCustSearch.clear();
        custTable.setItems(Customer.getAllCustomers());
        custTable.refresh();
        clearHideCust();
    }

    private void upcomingAlert() {
        for(Appointment appointment : Appointment.getAppointmentsDaily(LocalDate.now())) {
            if(appointment.getConsId() == userId && (appointment.getStartTime().toLocalTime().isAfter(LocalTime.now())
                    && appointment.getStartTime().toLocalTime().isBefore(LocalTime.now().plusMinutes(15)))) {
                VBox v = new VBox();
                v.setAlignment(Pos.CENTER);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initOwner(App.appWindow);
                alert.setTitle("Upcoming Appointment");
                alert.setHeaderText("You have an upcoming appointment!");
                alert.setContentText(appointment.getType() + " appointment with " +
                        appointment.getCustName() + " at " + appointment.getStartTime());
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    alert.close();
                }
                break;
            }
        }
    }

    private void clearShowAppt() {
        searchApptClearButton.setVisible(true);
    }
    private void clearShowCust() { searchCustClearButton.setVisible(true); }
    private void clearHideAppt() {
        searchApptClearButton.setVisible(false);
    }
    private void clearHideCust() { searchCustClearButton.setVisible(false); }

    public void initialize() {
        apptTable.setItems(Appointment.getAppointments());
        colConsultant.setCellValueFactory(new PropertyValueFactory<>("consName"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("custName"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colStartTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        colEndTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        custTable.setItems(Customer.getAllCustomers());
        colCustName.setCellValueFactory(new PropertyValueFactory<>("custName"));
        colCustAddress.setCellValueFactory(new PropertyValueFactory<>("custAddress"));
        colCustPhone.setCellValueFactory(new PropertyValueFactory<>("custPhone"));
        apptTable.getSortOrder().add(colDate);
        apptTable.refresh();
        upcomingAlert();
    }

}

