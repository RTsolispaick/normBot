package org.opp.core;

/**
 * Этот класс отвечает за обработку информации получаемой от юзера
 */
public class Logic {
    /**
     * Голый конструктор для объвления объекта внутри класса бота
     */
    public Logic() {};

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
