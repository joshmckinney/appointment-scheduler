package me.joshmckinney.scheduler.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import me.joshmckinney.scheduler.App;
import me.joshmckinney.scheduler.dao.AppointmentDao;
import me.joshmckinney.scheduler.model.Appointment;
import me.joshmckinney.scheduler.utils.Database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.*;
import java.util.Optional;

public class ApptManager {

    @FXML private Label apptManagerHeader = null;
    @FXML private Text tName;
    @FXML private ComboBox comboType;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox comboTime;
    @FXML private ComboBox comboLength;
    ObservableList<String> types = FXCollections.observableArrayList("Initial", "Consult", "Estimate", "Follow-up");
    ObservableList<String> times = FXCollections.observableArrayList(
            "09:00:00", "09:15:00", "09:30:00", "09:45:00",
            "10:00:00", "10:15:00", "10:30:00", "10:45:00",
            "11:00:00", "11:15:00", "11:30:00", "11:45:00",
            "12:00:00", "12:15:00", "12:30:00", "12:45:00",
            "13:00:00", "13:15:00", "13:30:00", "13:45:00",
            "14:00:00", "14:15:00", "14:30:00", "14:45:00",
            "15:00:00", "15:15:00", "15:30:00", "15:45:00",
            "16:00:00", "16:15:00", "16:30:00");
    ObservableList<String> durations = FXCollections.observableArrayList("15","30");

    private Connection conn;
    private Appointment appointment;
    private int apptId;
    private int consId;
    private int custId;
    private String intent;
    private String consName;
    private String custName;
    private String type = null;
    private Date date;
    private Time startTime;
    private Time endTime;
    private static final LocalTime businessStart = LocalTime.of(9,00);
    private static final LocalTime businessEnd = LocalTime.of(17,00);

    public void popAppointment(Appointment appointment) { this.appointment = appointment; };
    public void popApptId(int apptId) { this.apptId = apptId; };
    public void popConsId(int consId) { this.consId = consId; };
    public void popCustId(int custId) { this.custId = custId; };
    public void popConsName(String consName) { this.consName = consName; }
    public void popCustName(String custName) {
        this.custName = custName;
        this.tName.setText(custName);
    }
    public void popType(String type) {
        this.type = type;
        int typeIndex = 0;
        switch(type) {
            // "Initial", "Consult", "Estimate", "Follow-up"
            case "Initial":
                typeIndex = 0;
                break;
            case "Consult":
                typeIndex = 1;
                break;
            case "Estimate":
                typeIndex = 2;
                break;
            case "Follow-up":
                typeIndex = 3;
                break;
            default:
                typeIndex = 0;
                break;
        }
        comboType.getSelectionModel().select(typeIndex);
    }
    public void popDate(Date date) {
        this.date = date;
        datePicker.setValue(date.toLocalDate());
    }
    public void popStartTime(Time startTime) {
        this.startTime = startTime;
        String time = startTime.toString();
        int timeIndex = 0;
        switch(time) {
            case "09:00:00":
                timeIndex = 0;
                break;
            case "09:15:00":
                timeIndex = 1;
                break;
            case "09:30:00":
                timeIndex = 2;
                break;
            case "09:45:00":
                timeIndex = 3;
                break;
            case "10:00:00":
                timeIndex = 4;
                break;
            case "10:15:00":
                timeIndex = 5;
                break;
            case "10:30:00":
                timeIndex = 6;
                break;
            case "10:45:00":
                timeIndex = 7;
                break;
            case "11:00:00":
                timeIndex = 8;
                break;
            case "11:15:00":
                timeIndex = 9;
                break;
            case "11:30:00":
                timeIndex = 10;
                break;
            case "11:45:00":
                timeIndex = 11;
                break;
            case "12:00:00":
                timeIndex = 12;
                break;
            case "12:15:00":
                timeIndex = 13;
                break;
            case "12:30:00":
                timeIndex = 14;
                break;
            case "12:45:00":
                timeIndex = 15;
                break;
            case "13:00:00":
                timeIndex = 16;
                break;
            case "13:15:00":
                timeIndex = 17;
                break;
            case "13:30:00":
                timeIndex = 18;
                break;
            case "13:45:00":
                timeIndex = 19;
                break;
            case "14:00:00":
                timeIndex = 20;
                break;
            case "14:15:00":
                timeIndex = 21;
                break;
            case "14:30:00":
                timeIndex = 22;
                break;
            case "14:45:00":
                timeIndex = 23;
                break;
            case "15:00:00":
                timeIndex = 24;
                break;
            case "15:15:00":
                timeIndex = 25;
                break;
            case "15:30:00":
                timeIndex = 26;
                break;
            case "15:45:00":
                timeIndex = 27;
                break;
            case "16:00:00":
                timeIndex = 28;
                break;
            case "16:15:00":
                timeIndex = 29;
                break;
            case "16:30:00":
                timeIndex = 30;
                break;
            default:
                timeIndex = 0;
                break;
        }
        comboTime.getSelectionModel().select(startTime);
    }
    public void popEndTime(Time endTime) {
        this.endTime = endTime;
        LocalTime st = startTime.toLocalTime();
        LocalTime en = endTime.toLocalTime();
        if(st.plusMinutes(15) == en) {
            comboLength.getSelectionModel().select(0);
        } else {
            comboLength.getSelectionModel().select(1);
        }
    }

    public void setIntent(String intent) {
        if(intent == "new") {
            apptManagerHeader.setText("Create New Appointment");
            this.intent = "new";
        } else {
            apptManagerHeader.setText("Edit Existing Appointment");
            this.intent = "edit";
        }
    }

    @FXML private void saveButtonClick(ActionEvent event) throws NullPointerException, SQLException {
        Database.connect();
        conn = Database.getConn();
        if(App.isConnected) {
            try {
                type = comboType.getValue().toString();
                LocalDate date = datePicker.getValue();
                LocalDateTime startTime = LocalDateTime.of(date, LocalTime.parse(comboTime.getValue().toString()));
                String length = comboLength.getValue().toString();
                int duration = Integer.parseInt(length);
                LocalDateTime endTime = startTime.plusMinutes(duration);
                ZonedDateTime zonedStartTime = startTime.atZone(ZoneId.systemDefault());
                ZonedDateTime zonedConvertedStartTime = zonedStartTime.withZoneSameInstant(ZoneId.of("UTC"));
                LocalDateTime utcStartTime = zonedConvertedStartTime.toLocalDateTime();
                ZonedDateTime zonedEndTime = endTime.atZone(ZoneId.systemDefault());
                ZonedDateTime zonedConvertedEndTime = zonedEndTime.withZoneSameInstant(ZoneId.of("UTC"));
                LocalDateTime utcEndTime = zonedConvertedEndTime.toLocalDateTime();
                boolean isValid = false;

                if(intent == "edit") {
                    for(Appointment appointment : Appointment.getAppointmentsDaily(date)) {
                        // check edited date for consultant overlaps
                        if (appointment.getConsId() == consId) {
                            // Check for appt overlap -- Rubric Exception Control F2
                            if ((startTime.toLocalTime().isBefore(appointment.getEndTime().toLocalTime()))
                                    && (endTime.toLocalTime().isAfter(appointment.getStartTime().toLocalTime()))) {
                                isValid = false;
                                VBox v = new VBox();
                                v.setAlignment(Pos.CENTER);
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.initOwner(App.appWindow);
                                alert.setTitle("Error");
                                alert.setHeaderText("Appointment overlap detected!");
                                alert.setContentText(appointment.getConsName() + " already has a(n) " +
                                        appointment.getType() + " appointment with " + appointment.getCustName() +
                                        " at " + appointment.getStartTime() + " on " + appointment.getDate() + ".");
                                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                                Optional<ButtonType> result = alert.showAndWait();
                                if (result.get() == ButtonType.OK){
                                    alert.close();
                                    break;
                                }
                            } else {
                                isValid = true;
                            }
                        }
                        // check edited date for customer overlaps
                        if (appointment.getCustId() == custId) {
                            // Check for appt overlap -- Rubric Exception Control F2
                            if ((startTime.toLocalTime().isBefore(appointment.getEndTime().toLocalTime()))
                                    && (endTime.toLocalTime().isAfter(appointment.getStartTime().toLocalTime()))) {
                                isValid = false;
                                VBox v = new VBox();
                                v.setAlignment(Pos.CENTER);
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.initOwner(App.appWindow);
                                alert.setTitle("Error");
                                alert.setHeaderText("Appointment overlap detected!");
                                alert.setContentText(appointment.getCustName() + " already has a(n) " +
                                        appointment.getType() + " appointment with " + appointment.getConsName() +
                                        " at " + appointment.getStartTime() + " on " + appointment.getDate() + ".");
                                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                                Optional<ButtonType> result = alert.showAndWait();
                                if (result.get() == ButtonType.OK){
                                    alert.close();
                                    break;
                                }
                            } else {
                                isValid = true;
                            }
                        }
                    }
                    if(isValid) {
                        AppointmentDao.updateAppointment(apptId, type, utcStartTime, utcEndTime);
                        AppointmentDao.refreshAppointments();
                        conn.close();
                        Stage apptManager = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        apptManager.close();
                    }
                } else { // intent = new
                    // Create unique index
                    apptId = AppointmentDao.getApptIndex();
                    apptId++;
                    // If no appts are found allow to add one (don't proceed to check list).
                    if(Appointment.getAppointmentsDaily(date).isEmpty()) {
                        isValid = true;
                    } else {
                        // Check each appt for same consId and continue overlap check.
                        for (Appointment appointment : Appointment.getAppointmentsDaily(date)) {
                            // check edited date for consultant overlaps
                            if (appointment.getConsId() == consId) {
                                if ((startTime.toLocalTime().isBefore(appointment.getEndTime().toLocalTime()))
                                        && (endTime.toLocalTime().isAfter(appointment.getStartTime().toLocalTime()))) {
                                    isValid = false;
                                    VBox v = new VBox();
                                    v.setAlignment(Pos.CENTER);
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.initOwner(App.appWindow);
                                    alert.setTitle("Error");
                                    alert.setHeaderText("Appointment overlap detected!");
                                    alert.setContentText(appointment.getConsName() + " already has a(n) " + appointment.getType() + " appointment with " + appointment.getCustName() +
                                            " at " + appointment.getStartTime().toLocalTime() + " on " + appointment.getDate() + ".");
                                    alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                                    Optional<ButtonType> result = alert.showAndWait();
                                    if (result.get() == ButtonType.OK) {
                                        alert.close();
                                        break;
                                    }
                                } else {
                                    isValid = true;
                                }
                            }
                            // check edited date for customer overlaps
                            if (appointment.getCustId() == custId) {
                                if ((startTime.toLocalTime().isBefore(appointment.getEndTime().toLocalTime()))
                                        && (endTime.toLocalTime().isAfter(appointment.getStartTime().toLocalTime()))) {
                                    isValid = false;
                                    VBox v = new VBox();
                                    v.setAlignment(Pos.CENTER);
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.initOwner(App.appWindow);
                                    alert.setTitle("Error");
                                    alert.setHeaderText("Appointment overlap detected!");
                                    alert.setContentText(appointment.getCustName() + " already has a(n) " +
                                            appointment.getType() + " appointment with " + appointment.getConsName() +
                                            " at " + appointment.getStartTime() + " on " + appointment.getDate() + ".");
                                    alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                                    Optional<ButtonType> result = alert.showAndWait();
                                    if (result.get() == ButtonType.OK){
                                        alert.close();
                                        break;
                                    }
                                } else {
                                    isValid = true;
                                }
                            }
                        }
                    }
                    // Double check that appointment is between current local working hours -- Rubric Exception Control F1
                    // See also hoursCheck()
                    boolean businessHours;
                    if(hoursCheck(startTime.toLocalTime())) {
                        businessHours = true;
                    } else {
                        businessHours = false;
                        VBox v = new VBox();
                        v.setAlignment(Pos.CENTER);
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.initOwner(App.appWindow);
                        alert.setTitle("Error");
                        alert.setHeaderText("Appointment outside business hours!");
                        alert.setContentText("Please ensure the appointment start time falls within 9-5 localtime.");
                        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == ButtonType.OK){
                            alert.close();
                        }
                    }
                    if(isValid && businessHours) {
                        AppointmentDao.createAppointment(apptId, consId, custId, type, utcStartTime, utcEndTime);
                        AppointmentDao.refreshAppointments();
                        conn.close();
                        Stage apptManager = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        apptManager.close();
                    }
                }
            } catch (NullPointerException ex) {
                if(type == null) {
                    infoCatch("Type");
                } else if(datePicker.getValue() == null) {
                    infoCatch("Date");
                } else if(comboTime.getValue() == null) {
                    infoCatch("Time");
                } else if(comboLength.getValue() == null) {
                    infoCatch("Length");
                } else {
                    infoCatch("Unknown item");
                }
            }
        } else {
            Database.connectionError();
        }
    }

    @FXML private void cancelButtonClick(ActionEvent event) {
        Stage apptManager = (Stage) ((Node) event.getSource()).getScene().getWindow();
        apptManager.close();
    }

    private boolean hoursCheck(LocalTime time) {
        if((time.equals(businessStart) || time.isAfter(businessStart)) && (time.equals(businessEnd) || time.isBefore(businessEnd))) {
            return true;
        } else {
            return false;
        }
    }

    private void infoCatch(String exceptionItem) {
        VBox v = new VBox();
        v.setAlignment(Pos.CENTER);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(App.appWindow);
        alert.setTitle("Error");
        alert.setHeaderText("Invalid Appointment");
        if(exceptionItem.equals("NULL")) {
            alert.setContentText("Missing drop down selection, please check and try again.");
        } else {
            alert.setContentText(exceptionItem + " is missing or invalid, please check and try again.");
        }
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            alert.close();
        }
    }

    public void initialize() {
        comboType.setItems(types);
        comboTime.setItems(times);
        comboLength.setItems(durations);
    }
}