package com.potseluev.client;

import com.potseluev.common.ScheduleNotificationCommand;
import com.potseluev.common.NotificationType;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        scheduleEmail("First", "1", 1);
        scheduleEmail("Second", "2", 2);
        scheduleEmail("Third", "3", 3);
    }

    private static void scheduleEmail(String message, String externalId, int delayInMinutes) {
        try (
                Socket socket = new Socket("localhost", 4444);
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
        ) {
            HashMap<String, String> extraParams = new HashMap<>();
            extraParams.put("email", "potseluev.maksim@mail.ru");
            Date dNow = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(dNow);
            cal.add(Calendar.MINUTE, delayInMinutes);
            ScheduleNotificationCommand command = new ScheduleNotificationCommand(message, externalId, cal.getTime(),
                    NotificationType.EMAIL, extraParams);
            printWriter.println(command.encode());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
