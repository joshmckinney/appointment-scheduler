package me.joshmckinney.scheduler.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;

public class Log {
    public static void login(int userId) {
        try {
        Instant instant = Instant.now();
        String timestamp = instant.toString();
            File loginLog = new File("login_log.txt");
            if(loginLog.createNewFile()) {
                FileWriter loginWriter = new FileWriter("login_log.txt");
                loginWriter.write("SCHEDULER LOGIN LOG\n[UTC TIME]\n********************\n" +
                        "User #" + userId + " logged in at " + timestamp + "\n");
                loginWriter.close();
            } else {
                try {
                    FileWriter loginWriter = new FileWriter("login_log.txt", true);
                    loginWriter.write("User #" + userId + " logged in at " + timestamp + "\n");
                    loginWriter.close();
                } catch (IOException e) {
                    System.out.println("error:");
                    e.printStackTrace();
                }

            }
        } catch (IOException e) {
            System.out.println("error:");
            e.printStackTrace();
        }
    }
}