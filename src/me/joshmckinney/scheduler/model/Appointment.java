package me.joshmckinney.scheduler.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.sql.Time;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Calendar;

public class Appointment {
    private int apptId;
    private int consId;
    private int custId;
    private String consName;
    private String custName;
    private String type;
    private Date date;
    private Time startTime;
    private Time endTime;

    private static ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
    private static ObservableList<Appointment> appointmentListDaily = FXCollections.observableArrayList();
    private static ObservableList<Appointment> appointmentListWeekly = FXCollections.observableArrayList();
    private static ObservableList<Appointment> appointmentListMonthly = FXCollections.observableArrayList();
    private static ObservableList<Appointment> lookupAppointment = FXCollections.observableArrayList();

    public Appointment(int apptId, int consId, int custId, String consName, String custName, String type, Date date, Time startTime, Time endTime) {
        this.apptId = apptId;
        this.consId = consId;
        this.custId = custId;
        this.consName = consName;
        this.custName = custName;
        this.type = type;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getApptId() { return apptId; }
    public int getConsId() { return consId; }
    public int getCustId() { return custId; }
    public String getConsName() { return consName; }
    public String getCustName() { return custName; }
    public String getType() { return type; }
    public Date getDate() { return date; }
    public Time getStartTime() { return startTime; }
    public Time getEndTime() { return endTime; }
    public static ObservableList<Appointment> getAppointments() { return appointmentList; }
    public static ObservableList<Appointment> getLookupAppointment() { return lookupAppointment; }
    public static ObservableList<Appointment> getAppointmentsDaily(LocalDate day) {
        Calendar localCal = Calendar.getInstance();
        localCal.setTime(Date.valueOf(day));
        Calendar checkCal = Calendar.getInstance();
        for (Appointment appointment : appointmentList) {
            checkCal.setTime(Date.valueOf(appointment.getDate().toLocalDate()));
            if(localCal.get(Calendar.DAY_OF_YEAR) == checkCal.get(Calendar.DAY_OF_YEAR)) {
                Appointment.addAppointmentDaily(appointment);
            }
        }
        return appointmentListDaily;
    }
    public static ObservableList<Appointment> getAppointmentsWeekly() {
        Calendar localCal = Calendar.getInstance();
        localCal.setTime(java.util.Date.from(Instant.now()));
        Calendar checkCal = Calendar.getInstance();
        for (Appointment appointment : appointmentList) {
            checkCal.setTime(appointment.getDate());
            if(localCal.get(Calendar.WEEK_OF_YEAR) == checkCal.get(Calendar.WEEK_OF_YEAR)) {
                Appointment.addAppointmentWeekly(appointment);
            }
        }
        return appointmentListWeekly;
    }
    public static ObservableList<Appointment> getAppointmentsMonthly() {
        Calendar localCal = Calendar.getInstance();
        localCal.setTime(java.util.Date.from(Instant.now()));
        Calendar checkCal = Calendar.getInstance();
        for(Appointment appointment : appointmentList) {
            checkCal.setTime(appointment.getDate());
            if(localCal.get(Calendar.MONTH) == checkCal.get(Calendar.MONTH)) {
                Appointment.addAppointmentMonthly(appointment);
            }
        }
        return appointmentListMonthly;
    }
    public static void addAppointment(Appointment newAppointment) { appointmentList.add(newAppointment); }
    public static void addAppointmentDaily(Appointment newAppointment) { appointmentListDaily.add(newAppointment); }
    public static void addAppointmentWeekly(Appointment newAppointment) { appointmentListWeekly.add(newAppointment); }
    public static void addAppointmentMonthly(Appointment newAppointment) { appointmentListMonthly.add(newAppointment); }
    public static void deleteAllAppointments() {
        getAppointments().clear();
    }
    public static void deleteAllDailyAppointments() {
        getAppointmentsDaily(LocalDate.now()).clear();
    }
    public static void deleteAllWeeklyAppointments() {
        getAppointmentsWeekly().clear();
    }
    public static void deleteAllMonthlyAppointments() {
        getAppointmentsMonthly().clear();
    }
    public static ObservableList<Appointment> lookupAppointment(String custName) {
        if (!(getLookupAppointment().isEmpty())) {
            getLookupAppointment().clear();
        }
        for (Appointment appointment : getAppointments()) {
            if (appointment.getCustName().toLowerCase().contains(custName.toLowerCase())) {
                getLookupAppointment().add(appointment);
            }
        }
        return getLookupAppointment();
    }
}
