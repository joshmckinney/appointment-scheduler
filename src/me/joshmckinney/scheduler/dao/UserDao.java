package me.joshmckinney.scheduler.dao;

import me.joshmckinney.scheduler.model.User;
import me.joshmckinney.scheduler.utils.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDao {

    public static void refreshUsers() {
        User.deleteAllUsers();
        try {
            ResultSet rs = null;
            Connection conn = Database.getConn();
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM user;");
            int isActive = 0;
            int userId = 0;
            String userName = null;
            while (rs.next()) {
                userId = rs.getInt(1);
                userName = rs.getString(2);
                isActive = rs.getInt(4);
                if(isActive == 1) {
                    User.addUser(new User(userId, userName));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
