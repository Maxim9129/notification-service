package com.potseluev.server;

import com.potseluev.common.NotificationType;

import java.sql.*;
import java.util.*;

public class NotificationsDatabase {

    public NotificationsDatabase() {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS COMMAND" +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "MESSAGE               TEXT     NOT NULL," +
                "EXTERNAL_ID           VARCHAR(50) NOT NULL," +
                "DATE                  INTEGER     NOT NULL," +
                "NOTIFICATION_TYPE     INT      NOT NULL," +
                "EMAIL                 TEXT," +
                "URL                   TEXT)";
        try {
            Class.forName("org.sqlite.JDBC");

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try (
            Connection c = DriverManager.getConnection("jdbc:sqlite:test.db");
            Statement statement = c.createStatement();
        )
        {
            statement.executeUpdate(sqlCreate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Table create successfully.");



    }

    public void createNotification(Notification notification) {
        String sqlInsert = "INSERT INTO COMMAND (MESSAGE, EXTERNAL_ID, DATE, NOTIFICATION_TYPE, EMAIL, URL)" +
                " VALUES ('" + notification.getMessage() + "',"
                + notification.getExternalId() + ","
                + notification.getTime().getTime() + ","
                + notification.getNotificationType().getId() + ",'"
                + notification.getEmail() + "','"
                + notification.getUrl() + "');";
        try (
            Connection c = DriverManager.getConnection("jdbc:sqlite:test.db");
            Statement statement = c.createStatement();
        )
        {
            c.setAutoCommit(false) ;
            statement.executeUpdate(sqlInsert);
            c.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Notification> getPendingNotifications() {
        String sqlSelect = "SELECT * FROM COMMAND WHERE DATE < DATE()";
        ArrayList<Notification> notifications = new ArrayList<Notification>();
        try (
            Connection c = DriverManager.getConnection("jdbc:sqlite:test.db");
            Statement statement = c.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlSelect);
        )
        {
            c.setAutoCommit(false);
            while (resultSet.next())
            {
                Notification notification = new Notification();
                int id = resultSet.getInt("id");
                String message = resultSet.getString("message");
                int notification_type = resultSet.getInt("notification_type");
                long time = resultSet.getLong("date");
                String email = resultSet.getString("email");
                String url = resultSet.getString("url");
                notification.setId(id);
                notification.setMessage(message);
                notification.setNotificationType(NotificationType.valueOf(notification_type));
                notification.setEmail(email);
                notification.setUrl(url);
                System.out.println("ID = " + id);
                System.out.println( "MESSAGE = " + message);
                System.out.println( "notification_type = " + notification_type);

                System.out.println("date = " + new java.util.Date(time));
                System.out.println( "email = " + email);
                System.out.println( "url = " + url);
                System.out.println();
                notifications.add(notification);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }

    public void deleteNotification(long notificationId) {
        String sqlDelete = "DELETE FROM COMMAND WHERE ID = " + notificationId;
        try (
                Connection c = DriverManager.getConnection("jdbc:sqlite:test.db");
                Statement statement = c.createStatement();
        )
        {
            c.setAutoCommit(false) ;
            statement.executeUpdate(sqlDelete);
            c.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
