package com.potseluev.server;

import com.potseluev.common.Command;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Рустам on 17.07.2015.
 */
public class NotificationsDatabase {

    public NotificationsDatabase() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        String sql = "CREATE IF NOT EXIST TABLE COMMAND" +
                "(ID INT PRIMARY KEY    NOT NULL," +
                "MESSAGE               TEXT     NOT NULL," +
                "EXTERNAL_ID           CHAR(50) NOT NULL," +
                "DATE                  DATE     NOT NULL," +
                "NOTIFICATION_TYPE     INT      NOT NULL," +
                "EMAIL                 TEXT," +
                "URL                   TEXT)";

    }

    public void createNotification(Notification notification) {
        try {
            Connection c = DriverManager.getConnection("jdbc:sqlite:test.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Notification> getPendingNotifications() {
        return new ArrayList<>();
    }

    public void deleteNotification(long notificationId) {

    }
}
