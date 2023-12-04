package org.opp.core;

import org.opp.core.handler.GameSessionHandler;
import org.opp.core.handler.IdleHandler;
import org.opp.data.models.GameSession;
import org.opp.data.models.Response;
import org.opp.data.models.User;
import org.opp.data.models.types.TypeGame;
import org.opp.data.repositories.GameSessionRepository;

import java.util.List;

/**
 * Управление состоянием игры
 */

public class Manager {
    private final IdleHandler idleHandler;
    private final GameSessionHandler gameSessionHandler;
    private final GameSessionRepository gameSessionRepository;


    public Manager() {
        this.gameSessionHandler = new GameSessionHandler();
        this.idleHandler = new IdleHandler();
        this.gameSessionRepository = new GameSessionRepository();
    }

    /**
     * Конструктор класса для тестирования функциональности класса
     * @param gameSessionHandler замоканное состояние игры для корректного теста функциональности класса
     * @param idleHandler замоканный состояние покоя для корректного теста функциональности класса
     */
    public Manager(GameSessionHandler gameSessionHandler, IdleHandler idleHandler, GameSessionRepository gameSessionRepository) {
        this.gameSessionHandler = gameSessionHandler;
        this.idleHandler = idleHandler;
        this.gameSessionRepository = gameSessionRepository;
    }


    /**
     * Обработка сообщений в зависимости от состояния пользователя
     * @param message сообщение
     * @param user пользователь
     * @return Ответ на сообщение
     */
    public List<Response> chooseState(String message, User user) {
        return switch (user.getState()) {
            case IDLE -> {
                switch (message) {
                    case "/game" -> {
                        GameSession gameSessionForSingleGame = new GameSession(TypeGame.SINGLE);
                        yield gameSessionHandler.managerGameSession(message, gameSessionForSingleGame, user);
                    }
                    case "/multiplayer" -> {
                        GameSession gameSessionForDoubleGame = gameSessionRepository.getRandomGameSession();
                        if (gameSessionForDoubleGame == null)
                            gameSessionForDoubleGame = new GameSession(TypeGame.MULTI);
                        yield gameSessionHandler.managerGameSession(message, gameSessionForDoubleGame, user);
                    }
                    case "/stop" -> {
                        yield List.of(new Response(user,
                                "Вы не находитесь в игре!\nЕсли хотите начать игру напишите /game или /multiply"));
                    }
                    default -> {
                        yield List.of(new Response(user,
                                idleHandler.getResponse(message, user)));
                    }
                }
            }
            case GAME -> {
                if (message.equals("/game") || message.equals("/multiplayer")) {
                    yield List.of(new Response(user,
                            "Игра и так идёт!\nЕсли хотите остановить игру напишите /stop"));
                }

                yield gameSessionHandler.managerGameSession(message, user.getUserGameSession(), user);
            }
        };
    }
}
