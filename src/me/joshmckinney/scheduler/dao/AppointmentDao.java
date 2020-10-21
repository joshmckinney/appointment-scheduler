package me.joshmckinney.scheduler.dao;

import me.joshmckinney.scheduler.model.Appointment;
import me.joshmckinney.scheduler.ui.Main;
import me.joshmckinney.scheduler.utils.Database;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class AppointmentDao {

    private static int apptIndexCount;

    public static void refreshAppointments() {
        Appointment.deleteAllAppointments();
        Appointment.deleteAllDailyAppointments();
        Appointment.deleteAllWeeklyAppointments();
        Appointment.deleteAllMonthlyAppointments();
        try {
            ResultSet rs = null;
            Connection conn = Database.getConn();
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM appointmentview");
            while (rs.next()) {
                // Get data from appointmentview
                int apptId = rs.getInt(1);
                int consId = rs.getInt(2);
                int custId = rs.getInt(3);
                String consName = rs.getString(4);
                String custName = rs.getString(5);
                String apptType = rs.getString(6);
                Date date = rs.getDate(7);
                Time dbStartTime = rs.getTime(7);
                Time dbEndTime = rs.getTime(8);

                // Convert times to/from utc/local
                ZonedDateTime utcStart = ZonedDateTime.of(date.toLocalDate(), dbStartTime.toLocalTime(), ZoneId.of("UTC"));
                ZonedDateTime utcEnd = ZonedDateTime.of(date.toLocalDate(), dbEndTime.toLocalTime(), ZoneId.of("UTC"));
                ZonedDateTime localStart = utcStart.withZoneSameInstant(ZoneId.systemDefault());
                ZonedDateTime localEnd = utcEnd.withZoneSameInstant(ZoneId.systemDefault());
                Time startTime = Time.valueOf(LocalTime.from(localStart));
                Time endTime = Time.valueOf(LocalTime.from(localEnd));

                // Add each appointment object
                Appointment.addAppointment(new Appointment(apptId, consId, custId, consName, custName, apptType, date, startTime, endTime));
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        System.out.println("Appointment list refreshed!");
    }

    public static void deleteAppointment(int apptId) {
        try {
            ResultSet rs = null;
            Connection conn = Database.getConn();
            Statement stmt = conn.createStatement();
            stmt.execute("DELETE FROM appointment WHERE appointmentId='" + apptId + "';");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        AppointmentDao.refreshAppointments();
    }

    public static void deleteAppointmentByCustId(int custId) {
        try {
            ResultSet rs = null;
            Connection conn = Database.getConn();
            Statement stmt = conn.createStatement();
            stmt.execute("DELETE FROM appointment WHERE customerId='" + custId + "';");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        AppointmentDao.refreshAppointments();
    }

    public static void updateAppointment(int apptId, String type, LocalDateTime startTime, LocalDateTime endTime) {
        try {
            ResultSet rs = null;
            Connection conn = Database.getConn();
            Statement stmt = conn.createStatement();
            ZonedDateTime zdt = LocalDateTime.now().atZone(ZoneId.systemDefault());
            LocalDateTime updateTime = zdt.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
            stmt.executeUpdate("UPDATE appointment SET type='"+type+"', start='"+startTime+"', end='"+endTime+"', lastUpdate='"+updateTime+"', lastUpdateBy='"+Main.getSessionUserName()+"' WHERE appointmentId='"+apptId+"';");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createAppointment(int apptId, int consId, int custId, String apptType, LocalDateTime startTime, LocalDateTime endTime) {
        try {
            ResultSet rs = null;
            Connection conn = Database.getConn();
            Statement stmt = conn.createStatement();

            ZonedDateTime zdt = LocalDateTime.now().atZone(ZoneId.systemDefault());
            LocalDateTime createdTime = zdt.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
            LocalDateTime updateTime = createdTime;
            stmt.executeUpdate("INSERT INTO appointment VALUES (" +
                    apptId + "," +
                    custId + "," +
                    consId + "," +
                    "''," + // title - not needed
                    "''," + // desc - not needed
                    "''," + // location - not needed
                    "''," + // contact - not needed
                    "'"+apptType+"'," +
                    "''," + // url - not needed
                    "'"+startTime+"'," + // start time
                    "'"+endTime+"'," + // end time
                    "'"+createdTime+"'," + // create date
                    "'"+Main.getSessionUserName()+"'," + // created by
                    "'"+updateTime+"'," + // last update
                    "'"+Main.getSessionUserName()+"'" + // last updated by
                    ")");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static int getApptIndex() {
        try {
            ResultSet rs = null;
            Connection conn = Database.getConn();
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT MAX(apptId) FROM appointmentview;");
            while (rs.next()) {
                apptIndexCount = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return apptIndexCount;
    }
}