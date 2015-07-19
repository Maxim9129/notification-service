package com.potseluev.server;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class NotificationSender {

    private static final String CHARSET = StandardCharsets.UTF_8.name();
    private static final String SUBJECT = "Message from Notification service";
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String LOGIN = "ivanov.ivandows@gmail.com";
    private static final String PASSWORD = "uvocsu76";
    private static final String FROM = "a@a.com";

    public void sendNotification(Notification notification) throws SenderException {
        switch (notification.getNotificationType()) {
            case EMAIL:
                sendEmail(notification.getEmail(), notification.getMessage());
                break;
            case HTTP_REQUEST:
                sendHttp(notification.getUrl(), notification.getMessage());
                break;
        }
    }

    private Session getSession() {
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.host", SMTP_HOST);
        properties.setProperty("mail.smtp.port", SMTP_PORT);
        properties.setProperty("mail.smtp.starttls.enable", "true");
        return Session.getInstance(properties,
                    new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(LOGIN, PASSWORD);
                        }
                    });
    }

    public void sendEmail(String to, String message) throws SenderException {
        Session session;

        session = getSession();

        try {
            // Create a default MimeMessage object.
            MimeMessage mimeMessage = new MimeMessage(session);

            // Set From: header field of the header.
            mimeMessage.setFrom(new InternetAddress(FROM));

            // Set To: header field of the header.
            mimeMessage.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));

            // Set Subject: header field
            mimeMessage.setSubject(SUBJECT);

            // Now set the actual message
            mimeMessage.setText(message);

            // Send message
            Transport.send(mimeMessage);
        } catch (MessagingException mex) {
            throw new SenderException(mex.getMessage());
        }
    }

    public void sendHttp(String httpAddress, String message) throws SenderException {
        try {
            URLConnection connection = getConnectionToHttpAddress(httpAddress);
            try (OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream())) {
                String query = String.format("m=%s", URLEncoder.encode(message, CHARSET));
                out.write(query);
            }
        } catch (IOException e) {
            throw new SenderException(e);
        }
    }

    private URLConnection getConnectionToHttpAddress(String httpAddress) throws IOException {
        URL url = new URL(httpAddress);
        URLConnection connection = url.openConnection();
        connection.setRequestProperty("Accept-Charset", CHARSET);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;CHARSET=" + CHARSET);
        connection.setDoOutput(true);
        return connection;
    }
}
