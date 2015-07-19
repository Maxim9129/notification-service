package com.potseluev.server;


import com.potseluev.common.Command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {

    private static int PORT = 4444;
    private static  long THREAD_SLEEP_MILISECONDS = 10000;

    public static void main(String[] args) throws ClassNotFoundException {

        NotificationsDatabase database = new NotificationsDatabase();
        NotificationSender notificationSender = new NotificationSender();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    List<Notification> notifications = database.getPendingNotifications();
                    for (Notification list : notifications) {
                        try {
                            notificationSender.sendNotification(list);
                        } catch (SenderException e) {
                            System.out.println(String.format("Failed to send the message %s.", e.getMessage()));
                        }
                        database.deleteNotification(list.getId());
                    }

                    try {
                        TimeUnit.MILLISECONDS.sleep(THREAD_SLEEP_MILISECONDS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                try (
                        Socket clientSocket = serverSocket.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                ) {
                    String line = in.readLine();
                    Command command = Command.decode(line);
                    Notification notification = new Notification();
                    notification.setMessage(command.getMessage());
                    notification.setExternalId(command.getExternalId());
                    notification.setNotificationType(command.getNotificationType());
                    notification.setTime(command.getTime());
                    if (command.getExtraParams().containsKey("email"))
                        notification.setEmail(command.getExtraParams().get("email"));
                    if (command.getExtraParams().containsKey("url"))
                        notification.setUrl(command.getExtraParams().get("url"));

                    database.createNotification(notification);
                    System.out.println(command.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
