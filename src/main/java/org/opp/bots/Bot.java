package org.opp.bots;

/**
 * Интерфейс бота
 */
public interface Bot {
    /**
     * Метод для отправки сообщений
     * @param id
     * @param message
     */
    public void sendMessage(Long id, String message);
}
