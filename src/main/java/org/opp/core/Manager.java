package org.opp.core;

import org.opp.core.handler.GameHandler;
import org.opp.core.handler.IdleHandler;
import org.opp.data.models.User;

/**
 * Управление состоянием игры
 */

public class Manager {
    private final IdleHandler idleHandler;
    private final GameHandler gameHandler;


    public Manager() {
        this.gameHandler = new GameHandler();
        this.idleHandler = new IdleHandler();
    }
    /**
     * Конструктор класса для тестирования функциональности класса
     * @param gameHandler замоканное состояние игры для корректного теста функциональности класса
     * @param idleHandler замоканный состояние покоя для корректного теста функциональности класса
     */
    public Manager(GameHandler gameHandler, IdleHandler idleHandler) {
        this.gameHandler = gameHandler;
        this.idleHandler = idleHandler;
    }


    /**
     * Обработка сообщений в зависимости от состояния пользователя
     * @param message сообщение
     * @param user пользователь
     * @return Ответ на сообщение
     */
    public String chooseState(String message, User user) {
        return switch (user.getState()) {
            case IDLE -> {
                if (message.equals("/game")) {
                    yield gameHandler.updateStateUser(message, user);
                } else if (message.equals("/stop")) {
                    yield "Вы не находитесь в игре!\nЕсли хотите начать игру напишите /game";
                } else {
                    yield idleHandler.getResponse(message, user);
                }
            }
            case GAME -> {
                if (message.equals("/stop")) {
                    yield idleHandler.getResponse(message, user);
                } else if (message.equals("/game")) {
                    yield "Игра и так идёт!\nЕсли хотите остановить игру напишите /stop";
                } else {
                    yield gameHandler.updateStateUser(message, user);
                }
            }
        };
    }


}