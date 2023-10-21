package org.opp.core.handler;

/**
 * Этот класс отвечает за обработку информации получаемой от юзера
 */
public class IdleHandler {
    /**
     * Обрабатывает сообщения юзера, содержащие текстовое сообщение, и формирует ответ на него
     *
     * @param message содержит текст сообщения
     * @return возращает ответ на сообщение
     */
    public String massageHandler(String message) {
        switch (message) {
            case "/start": return "Hello, write something!";
            case "/help": return "Just write something.";
            default: return message;
        }
    }
}
