package com.potseluev.server;


import com.potseluev.common.Command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException {
        int portNumber = 4444;
        try(
                ServerSocket serverSocket = new ServerSocket(portNumber);
                )
        {
            while(true)
            {
                try (
                        Socket clientSocket = serverSocket.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                ) {
                    String line = in.readLine();
                    Command command = Command.decode(line);
                    System.out.println(command.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
