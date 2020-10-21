package me.joshmckinney.scheduler.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class User {

    private int userId;
    private String userName;

    private static ObservableList<User> userList = FXCollections.observableArrayList();
    private static List userNameList = new ArrayList<String>(); // just the names for use with report dialog

    public User(int userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public static ObservableList<User> getUsers() { return userList; }

    public static List<String> getNames() {
        userNameList.clear();
        for (User user : userList) {
            String userName = user.getUserName();
            userNameList.add(userName);
        }
        return userNameList;
    }

    public String getUserName() {
        return this.userName;
    }

    public static void addUser(User newUser) {
        userList.add(newUser);
    }

    public static void deleteAllUsers() {
        userList.clear();
    }
}
