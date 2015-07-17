package com.potseluev.common;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;

public class Command {
    private String message;
    private String externalId;
    private Date time;
    private NotificationType notificationType;
    private HashMap<String, String> extraParams;

    private Command() {
    }

    public Command(String message, String externalId, Date time, NotificationType notificationType, HashMap<String, String> extraParams) {
        this.message = message;
        this.externalId = externalId;
        this.time = time;
        this.notificationType = notificationType;
        this.extraParams = extraParams;
    }

    private static String encodeCommand(HashMap<String, String> params) {
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

    private static HashMap<String, String> decodeCommand(String encoded) {
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

    public String getMessage() {
        return message;
    }

    public String getExternalId() {
        return externalId;
    }

    public Date getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "Command{" +
                "message='" + message + '\'' +
                ", externalId='" + externalId + '\'' +
                ", time=" + time +
                ", notificationType=" + notificationType +
                ", extraParams=" + extraParams +
                '}';
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public HashMap<String, String> getExtraParams() {
        return extraParams;
    }

    public String encode(){
        HashMap<String, String> fieldValues = new HashMap<>();
        fieldValues.put("message", message);
        fieldValues.put("externalId", externalId);
        fieldValues.put("time", Long.toString(time.getTime()));
        fieldValues.put("notificationType", notificationType.toString());
        for (String key : extraParams.keySet())
        {
            fieldValues.put("extra_" + key, extraParams.get(key));
        }
        return encodeCommand(fieldValues);
    }

    public static Command decode(String encoded)
    {
        HashMap<String, String> fieldValues = decodeCommand(encoded);
        Command command = new Command();
        command.message = fieldValues.get("message");
        command.externalId = fieldValues.get("externalId");

        command.time = new Date(Long.parseLong(fieldValues.get("time")));
        command.notificationType = Enum.valueOf(NotificationType.class, fieldValues.get("notificationType"));
        command.extraParams = new HashMap<>();
        for (String key : fieldValues.keySet())
        {
            if (key.startsWith("extra_"))
            {
                command.extraParams.put(key.substring(6), fieldValues.get(key));
            }
        }
        return command;
    }
}
