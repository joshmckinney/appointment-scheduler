package me.joshmckinney.scheduler.dao;

import me.joshmckinney.scheduler.model.Customer;
import me.joshmckinney.scheduler.ui.Main;
import me.joshmckinney.scheduler.utils.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class CustomerDao {

    private static int custIndexCount;

    public static void refreshCustomers() {
        Customer.deleteAllCustomers();
        Customer.deleteAllInactiveCustomers();
        try {
            ResultSet rs = null;
            Connection conn = Database.getConn();
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM customerview;");
            while (rs.next()) {
                int custId = rs.getInt(1);
                boolean isActive = false;
                int activeCheck = rs.getInt(2);
                if(activeCheck==1) {
                    isActive = true;
                } else {
                    isActive = false;
                }
                String custName = rs.getString(3);
                String custAddress = rs.getString(4);
                String custPhone = rs.getString(5);
                if(isActive) {
                    Customer.addCustomer(new Customer(custId, isActive, custName, custAddress, custPhone));
                } else {
                    Customer.addInactiveCustomer(new Customer(custId,isActive,custName,custAddress,custPhone));
                }
            }
        } catch (Exception e) {

        }
        System.out.println("Customer list refreshed!");
    }
    public static void updateCustomer(int id, String name, String address, String phone) {
        try {
            ResultSet rs = null;
            Connection conn = Database.getConn();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("UPDATE customer SET customerName='"+name+"' WHERE customer.customerId='"+id+"';");
            stmt.executeUpdate("UPDATE address SET address='"+address+"', phone='"+phone+"' WHERE addressId='"+id+"';");
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }
    public static void createCustomer(String name, String address, String phone) {
        try {
            getCustIndex();
            custIndexCount++;
            int newId = custIndexCount;
            LocalDateTime timestamp = LocalDateTime.ofInstant(Instant.now(), ZoneId.of("UTC"));
            ResultSet rs = null;
            Connection conn = Database.getConn();
            Statement stmt = conn.createStatement();
            CustomerDao.refreshCustomers();
            stmt.executeUpdate("INSERT INTO address VALUES ("+newId+",'"+address+"','',1,'11111','"+phone+"','"+timestamp+"','"+Main.getSessionUserName()+"','"+timestamp+"','"+Main.getSessionUserName()+"');");
            stmt.executeUpdate("INSERT INTO customer VALUES ("+newId+",'"+name+"',1,1,'"+timestamp+"','"+Main.getSessionUserName()+"','"+timestamp+"','"+Main.getSessionUserName()+"');");
        } catch (SQLException e) {
            System.out.println(e.getSQLState().toString());
        }
    }
    public static void deleteCustomer(int customerId) {
        try {
            ResultSet rs = null;
            Connection conn = Database.getConn();
            Statement stmt = conn.createStatement();
            stmt.execute("UPDATE customer SET active=0 WHERE customerId='"+customerId+"';");
        } catch (SQLException e) {
            System.out.println(e.getSQLState().toString());
        }
        CustomerDao.refreshCustomers();
    }
    public static void undeleteCustomer(int customerId) {
        try {
            ResultSet rs = null;
            Connection conn = Database.getConn();
            Statement stmt = conn.createStatement();
            stmt.execute("UPDATE customer SET active=1 WHERE customerId='"+customerId+"';");
        } catch (SQLException e) {
            System.out.println(e.getSQLState().toString());
        }
        CustomerDao.refreshCustomers();
    }
    public static int getCustIndex() {
        try {
            ResultSet rs = null;
            Connection conn = Database.getConn();
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(*) FROM customerview;");
            while (rs.next()) {
                custIndexCount = rs.getInt(1);
            }
        } catch (Exception e) {

        }
        return custIndexCount;
    }

}
