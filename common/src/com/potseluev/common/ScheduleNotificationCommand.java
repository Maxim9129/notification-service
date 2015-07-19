package com.potseluev.common;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;

public class ScheduleNotificationCommand {

    public static final String PROPERTY_EMAIL = "email";
    public static final String PROPERTY_URL = "url";

    private static final String FIELD_EXTERNAL_ID = "externalId";
    private static final String FIELD_TIME = "time";
    private static final String FIELD_NOTIFICATION_TYPE = "notificationType";
    private static final String FIELD_MESSAGE = "message";
    private static final String EXTRA_PREFIX = "extra_";

    private static final String ENCODING = "UTF-8";
    private static final String FIELDS_DELIMITER = "&";
    private static final String KEY_VALUE_DELIMITER = "=";

    private String message;
    private String externalId;
    private Date time;
    private NotificationType notificationType;
    private HashMap<String, String> extraParams;

    private ScheduleNotificationCommand() {
    }

    public ScheduleNotificationCommand(String message, String externalId, Date time, NotificationType notificationType, HashMap<String, String> extraParams) {
        this.message = message;
        this.externalId = externalId;
        this.time = time;
        this.notificationType = notificationType;
        this.extraParams = extraParams;
    }

    private static String encodeCommand(HashMap<String, String> params) {
        String result = "";

        boolean first = true;
        for (String key : params.keySet()) {
            try {
                result += (!first ? FIELDS_DELIMITER : "") + key + KEY_VALUE_DELIMITER + URLEncoder.encode(params.get(key), ENCODING);
            } catch (UnsupportedEncodingException ignored) {
            }

            first = false;
        }

        return result;
    }

    private static HashMap<String, String> decodeCommand(String encoded) {
        HashMap<String, String> params = new HashMap<>();

        String[] parts = encoded.split(FIELDS_DELIMITER);
        for (String part : parts) {
            String[] keyValue = part.split(KEY_VALUE_DELIMITER);
            String key = keyValue[0];
            String value = null;
            try {
                value = URLDecoder.decode(keyValue[1], ENCODING);
            } catch (UnsupportedEncodingException ignored) {
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

    public String encode() {
        HashMap<String, String> fieldValues = new HashMap<>();
        fieldValues.put(FIELD_MESSAGE, message);
        fieldValues.put(FIELD_EXTERNAL_ID, externalId);
        fieldValues.put(FIELD_TIME, Long.toString(time.getTime()));
        fieldValues.put(FIELD_NOTIFICATION_TYPE, notificationType.toString());
        for (String key : extraParams.keySet()) {
            fieldValues.put(EXTRA_PREFIX + key, extraParams.get(key));
        }
        return encodeCommand(fieldValues);
    }

    public static ScheduleNotificationCommand decode(String encoded) {
        HashMap<String, String> fieldValues = decodeCommand(encoded);
        ScheduleNotificationCommand command = new ScheduleNotificationCommand();
        command.message = fieldValues.get(FIELD_MESSAGE);
        command.externalId = fieldValues.get(FIELD_EXTERNAL_ID);

        command.time = new Date(Long.parseLong(fieldValues.get(FIELD_TIME)));
        command.notificationType = Enum.valueOf(NotificationType.class, fieldValues.get(FIELD_NOTIFICATION_TYPE));
        command.extraParams = new HashMap<>();
        for (String key : fieldValues.keySet()) {
            if (key.startsWith(EXTRA_PREFIX)) {
                command.extraParams.put(key.substring(EXTRA_PREFIX.length()), fieldValues.get(key));
            }
        }

        return command;
    }
}
