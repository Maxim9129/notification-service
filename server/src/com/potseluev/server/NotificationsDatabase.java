package com.potseluev.server;

import com.potseluev.common.NotificationType;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class NotificationsDatabase {

    private static final String CONNECTION_STRING = "jdbc:sqlite:test.db";

    public NotificationsDatabase() {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS NOTIFICATION" +
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
                Connection c = getConnection();
                Statement statement = c.createStatement();
        ) {
            statement.executeUpdate(sqlCreate);
            c.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createNotification(Notification notification) {
        String sqlInsert = "INSERT INTO NOTIFICATION (MESSAGE, EXTERNAL_ID, DATE, NOTIFICATION_TYPE, EMAIL, URL)" +
                " VALUES (?, ?, ?, ?, ?, ?);";

        try (
                Connection c = getConnection();
                PreparedStatement statement = c.prepareStatement(sqlInsert);
        ) {
            statement.setString(1, notification.getMessage());
            statement.setString(2, notification.getExternalId());
            statement.setLong(3, notification.getTime().getTime());
            statement.setInt(4, notification.getNotificationType().getId());
            statement.setString(5, notification.getEmail());
            statement.setString(6, notification.getUrl());

            statement.execute();
            c.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(CONNECTION_STRING);
        connection.setAutoCommit(false);
        return connection;
    }

    public List<Notification> getPendingNotifications() {
        String sqlSelect = "SELECT * FROM NOTIFICATION WHERE DATE < " + System.currentTimeMillis();
        ArrayList<Notification> notifications = new ArrayList<>();
        try (
                Connection c = getConnection();
                Statement statement = c.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlSelect);
        ) {
            c.setAutoCommit(false);
            while (resultSet.next()) {
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
                notification.setTime(new Date(time));
                notifications.add(notification);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }

    public void deleteNotification(long notificationId) {
        String sqlDelete = "DELETE FROM NOTIFICATION WHERE ID = ?";
        try (
                Connection c = getConnection();
                PreparedStatement statement = c.prepareStatement(sqlDelete);
        ) {
            statement.setLong(1, notificationId);
            statement.execute();
            c.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
