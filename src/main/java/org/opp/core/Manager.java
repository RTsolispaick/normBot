package org.opp.core;

import org.opp.core.handler.GameHandler;
import org.opp.core.handler.IdleHandler;
import org.opp.data.StorageWord;
import org.opp.essence.User;

    /**
     * Управление состоянием игры
     */

public class Manager {
    private final IdleHandler idleHandler;
    private final GameHandler gameHandler;
    private final StorageWord storageWord;

    public Manager() {
        gameHandler = new GameHandler();
        idleHandler = new IdleHandler();
        storageWord = new StorageWord();
    }

    /**
     * Обработка сообщений в зависимости от состояния пользователя
     * @param message сообщение
     * @param user пользователь
     * @return Ответ на сообщение
     */
    public String chooseState(String message, User user) {
        String response;
        return switch (user.getState()) {
            case IDLE -> {
                if (message.equals("/game")) {
                    user.setStateGame(storageWord.wordChoice());
                    response = gameHandler.getAnswer(message, user);
                } else if (message.equals("/stop")) {
                    response = "Вы не находитесь в игре!\nЕсли хотите начать игру напишите /game";
                } else {
                    response = idleHandler.getAnswer(message);
                }
                yield response;
            }
            case GAME -> {
                if (message.equals("/stop")) {
                    user.setStateIdle();
                    response = idleHandler.getAnswer(message);
                } else if (message.equals("/game")) {
                    response = "Игра и так идёт!\nЕсли хотите остановить игру напишите /stop";
                } else {
                    response = gameHandler.getAnswer(message, user);
                    if (response.contains("/game")) {
                        user.setStateIdle();
                    }
                }
                yield response;
            }
        };
    }
}