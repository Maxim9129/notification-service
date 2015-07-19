package com.potseluev.client;

import com.potseluev.common.Command;
import com.potseluev.common.NotificationType;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try (
            Socket socket = new Socket("localhost", 4444);
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
        )
        {
            Date dNow = new Date(); // Instantiate a Date object
            Calendar cal = Calendar.getInstance();
            cal.setTime(dNow);
            cal.add(Calendar.MINUTE, 1);

            HashMap<String, String> extraParams = new HashMap<>();
            extraParams.put("url", "http://yandex.ru");
            Command command = new Command("Hello world", "1", cal.getTime(), NotificationType.HTTP_REQUEST, extraParams);
            printWriter.println(command.encode());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


}
