package org.opp.core;

public class Logic {
    public static String massageHandler(String message) {
        switch (message) {
            case "/start": return "Hello, write something!";
            case "/help": return "Just write something.";
            default: return message;
        }
    }
}
