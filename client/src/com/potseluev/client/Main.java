package com.potseluev.client;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        try (
            Socket socket = new Socket("localhost", 4444);
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        )
        {
            HashMap<String, String> params = new HashMap<>();
            params.put("email", "rustam2890@gmail.com");
            params.put("message", "Hello World");

            printWriter.println(Main.encodeCommand(params));
            System.out.println(bufferedReader.readLine());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String encodeCommand(HashMap<String, String> params) {
        String result = "";

        boolean first = true;
        for(String key : params.keySet()) {
            try {
                result += (!first ? "&" : "") + key + "=" + URLEncoder.encode(params.get(key), "UTF-8");
            } catch (UnsupportedEncodingException e) {
            }

            first = false;
        }

        return result;
    }

    public static HashMap<String, String> decodeCommand(String encoded) {
        HashMap<String, String> params = new HashMap<>();

        String[] parts = encoded.split("&");
        for (String part: parts) {
            String[] keyValue = part.split("=");
            String key = keyValue[0];
            String value = null;
            try {
                value = URLDecoder.decode(keyValue[1], "UTF-8");
            } catch (UnsupportedEncodingException e) {
            }

            params.put(key, value);
        }

        return params;
    }


}
