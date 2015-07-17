package com.potseluev.server;


import com.potseluev.common.Command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) {
	    // write your code here
        int portNumber = 4444;

        try (
            ServerSocket serverSocket = new ServerSocket(portNumber);
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
