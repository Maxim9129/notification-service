package com.potseluev.server;

public class SenderException extends Exception{
    public SenderException(String message) {
        super(message);
    }

    public SenderException(Exception e) {
        super(e);
    }
}
