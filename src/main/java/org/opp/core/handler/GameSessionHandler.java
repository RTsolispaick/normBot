package org.opp.core.handler;

import org.opp.data.models.Game;
import org.opp.data.models.GameSession;
import org.opp.data.models.Response;
import org.opp.data.models.User;
import org.opp.data.models.types.StateSession;
import org.opp.data.repositories.GameSessionRepository;
import org.opp.data.repositories.WordRepository;

import java.util.List;

/**
 * Контроль игровой сессии
 */
public class GameSessionHandler {
    private final GameSessionRepository gameSessionRepository;
    private final WordRepository wordRepository;
    private final GameHandler gameHandler;

    public GameSessionHandler() {
        this.gameSessionRepository = new GameSessionRepository();
        this.wordRepository = new WordRepository();
        this.gameHandler = new GameHandler();
    }

    /**
     * Конструктор для тестов
     * @param gameSessionRepository замоканный {@link GameSessionRepository}
     * @param wordRepository замоканный {@link WordRepository}
     */
    public GameSessionHandler(GameSessionRepository gameSessionRepository, WordRepository wordRepository) {
        this.gameSessionRepository = gameSessionRepository;
        this.wordRepository = wordRepository;
        this.gameHandler = new GameHandler();
    }

    /**
     * Обработка сообщения в зависимости от типа сессии и отсановка сессии по требованию
     * @param message сообщение пользователя
     * @param gameSession сессия, к которой обращается пользователь
     * @return список сообщений, которые адресуются пользователям, подключённым к сессии
     */
    public List<Response> managerGameSession(String message, GameSession gameSession, User user) {
        //TODO

        return null;
    }

    /**
     * Управляет сессией для однопользовательской игры
     * @param message сообщение пользователя
     * @param gameSession сессия для однопользовательской игры, к которой общается пользователь
     * @return список сообщений, которые адресуются пользователю, подключённому к сессии
     */
    protected List<Response> handleSingleSession(String message, GameSession gameSession, User user) {
        //TODO

        return null;
    }

    /**
     * Управляет сессией для многопользовательской игры
     * @param message сообщение пользователя
     * @param gameSession сессия для многопользовательской игры, к которой общается пользователь
     * @return список сообщений, которые адресуются пользователям, подключённым к сессии
     */
    protected List<Response> handleMultiSession(String message, GameSession gameSession, User user) {
        //TODO

        return null;
    }

    /**
     * Обработка многопользовательской сессии в состоянии {@link StateSession#INIT_SESSION}
     * @return список сообщений, которые адресуются пользователям, подключённым к сессии
     */
    protected List<Response> handleInitState(GameSession gameSession, User user) {
        //TODO

        return null;
    }

    /**
     * Обработка многопользовательской сессии в состоянии {@link StateSession#WAITING_FOR_PLAYERS}
     * @return список сообщений, которые адресуются пользователям, подключённым к сессии
     */
    protected List<Response> handleWaitingForPlayersState(GameSession gameSession, User user) {
        //TODO

        return null;
    }

    /**
     * Обработка многопользовательской сессии в состоянии {@link StateSession#MAKE_WORD}
     * @return список сообщений, которые адресуются пользователям, подключённым к сессии
     */
    protected List<Response> handleMakeWordState(String message, GameSession gameSession, User user, Game game) {
        //TODO

        return null;
    }

    /**
     * Обработка многопользовательской сессии в состоянии {@link StateSession#GAME}
     * @return список сообщений, которые адресуются пользователям, подключённым к сессии
     */
    protected List<Response> handleGameState(String message, GameSession gameSession, User user, Game game) {
        //TODO

        return null;
    }

    /**
     * Обрабатывает комманду /stop
     * @param gameSession останавливаемая сессия
     * @param user пользователь, который останавливает сессию
     * @return список сообщений, которые адресуются пользователям, подключённым к сессии
     */
    protected List<Response> stopSession(GameSession gameSession, User user) {
        //TODO

        return null;
    }
}