package me.joshmckinney.scheduler.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import me.joshmckinney.scheduler.model.Appointment;

import java.sql.Date;
import java.sql.Time;

public class Report {
    private static String typeReport;
    private static ObservableList<Appointment> reportList = FXCollections.observableArrayList();

    @FXML private TableView<Appointment> reportTable;
    @FXML private TableColumn<Appointment, String> colConsultant;
    @FXML private TableColumn<Appointment, String> colCustomer;
    @FXML private TableColumn<Appointment, String> colType;
    @FXML private TableColumn<Appointment, Date> colDate;
    @FXML private TableColumn<Appointment, Time> colStartTime;
    @FXML private TableColumn<Appointment, Time> colEndTime;

    public static String getTypeReport() { return typeReport; }

    public static ObservableList<Appointment> getReportList() {
        return reportList;
    }

    public void setTable() {
        reportTable.setItems(this.getReportList());
    }

    public static String typeReport() {
        int initial = 0;
        int consult = 0;
        int estimate = 0;
        int followup = 0;
        for(Appointment appointment : Appointment.getAppointmentsMonthly()) {
            String countType = appointment.getType();
            switch(countType) {
                case "Initial":
                    initial++;
                case "Consult" :
                    consult++;
                case "Estimate":
                    estimate++;
                case "Follow-Up":
                    followup++;
            }
        }
        typeReport =
                initial + " Initial\n" +
                consult + " Consult\n" +
                estimate + " Estimate\n" +
                followup + " Follow-Up";
        return typeReport;
    }

    public void consReport(String name) {
        reportList.clear();
        for(Appointment appointment : Appointment.getAppointments()) {
            if(name.equals(appointment.getConsName())) {
                reportList.add(appointment);
            }
        }
    }

    public static int getWeeklyCount() {
        int weeklyCount = 0;
        for(Appointment appointment : Appointment.getAppointmentsWeekly()) {
            weeklyCount++;
        }
        return weeklyCount;
    }

    public void initialize() {
        colConsultant.setCellValueFactory(new PropertyValueFactory<>("consName"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("custName"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colStartTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        colEndTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        reportTable.getSortOrder().add(colDate);
    }
}
