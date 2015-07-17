package com.potseluev.client;

import com.potseluev.common.Command;
import com.potseluev.common.NotificationType;

import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        try (
            Socket socket = new Socket("localhost", 4444);
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
        )
        {
            HashMap<String, String> extraParams = new HashMap<>();
            extraParams.put("url", "yandex.ru");
            Command command = new Command("Hello world", "1", new Date(), NotificationType.HTTP_REQUEST, extraParams);
            printWriter.println(command.encode());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


}
