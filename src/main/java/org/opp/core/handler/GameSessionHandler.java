package org.opp.core.handler;

import org.opp.data.models.Game;
import org.opp.data.models.GameSession;
import org.opp.data.models.Response;
import org.opp.data.models.User;
import org.opp.data.models.types.State;
import org.opp.data.models.types.StateSession;
import org.opp.data.models.types.StatusGame;
import org.opp.data.repositories.GameSessionRepository;
import org.opp.data.repositories.WordRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
        if (message.equals("/stop"))
            return stopSession(gameSession, user);

        return switch (gameSession.getTypeGame()) {
            case SINGLE -> handleSingleSession(message, gameSession, user);
            case MULTI -> handleMultiSession(message, gameSession, user);
        };
    }

    /**
     * Управляет сессией для однопользовательской игры
     * @param message сообщение пользователя
     * @param gameSession сессия для однопользовательской игры, к которой общается пользователь
     * @return список сообщений, которые адресуются пользователю, подключённому к сессии
     */
    private List<Response> handleSingleSession(String message, GameSession gameSession, User user) {
        Game game = gameSession.getGame();

        if (gameSession.getStateSession().equals(StateSession.INIT_SESSION)) {
            user.setStateGame(gameSession);
            game.initSingleGame(wordRepository.getRandomWord());
            gameSession.setStateSession(StateSession.GAME);
        }

        Response response = new Response(user,
                gameHandler.updateStatsUser(message, user, game));

        if (game.getStatusGame().equals(StatusGame.LOSEGAME)
                || game.getStatusGame().equals(StatusGame.WINGAME)) gameSessionRepository.delete(gameSession);

        return List.of(response);
    }

    /**
     * Управляет сессией для многопользовательской игры
     * @param message сообщение пользователя
     * @param gameSession сессия для многопользовательской игры, к которой общается пользователь
     * @return список сообщений, которые адресуются пользователям, подключённым к сессии
     */
    private List<Response> handleMultiSession(String message, GameSession gameSession, User user) {
        Game game = gameSession.getGame();

        return switch (gameSession.getStateSession()) {
            case INIT_SESSION -> handleInitState(gameSession, user);
            case WAITING_FOR_PLAYERS -> handleWaitingForPlayersState(gameSession, user);
            case MAKE_WORD -> handleMakeWordState(message, gameSession, user, game);
            case GAME -> handleGameState(message, gameSession, user, game);
        };
    }

    /**
     * Обработка многопользовательской сессии в состоянии {@link StateSession#INIT_SESSION}
     * @return список сообщений, которые адресуются пользователям, подключённым к сессии
     */
    private List<Response> handleInitState(GameSession gameSession, User user) {
        user.setStateGame(gameSession);
        gameSession.setStateSession(StateSession.WAITING_FOR_PLAYERS);
        return List.of(new Response(user,
                "Игра создана! Ищем вам соперника..."));
    }

    /**
     * Обработка многопользовательской сессии в состоянии {@link StateSession#WAITING_FOR_PLAYERS}
     * @return список сообщений, которые адресуются пользователям, подключённым к сессии
     */
    private List<Response> handleWaitingForPlayersState(GameSession gameSession, User user) {
        if (user.getState().equals(State.GAME)) {
            return List.of(new Response(user,
                    "Подожди второго игрока!\nЕсли ты не хочешь ждать напиши /stop"));
        }

        user.setStateGame(gameSession);
        gameSession.setStateSession(StateSession.MAKE_WORD);
        return List.of(new Response(user,
                        "Игра найдена!\nВаш соперник: " + gameSession.getAnotherUser(user).getName()),
                new Response(gameSession.getAnotherUser(user),
                        "Ваш соперник найден!\nВаш соперник: " + user.getName()),
                new Response(user,
                        "Загадайте слово!"),
                new Response(gameSession.getAnotherUser(user),
                        "Cлово загадывает ваш оппонент!"));
    }

    /**
     * Обработка многопользовательской сессии в состоянии {@link StateSession#MAKE_WORD}
     * @return список сообщений, которые адресуются пользователям, подключённым к сессии
     */
    private List<Response> handleMakeWordState(String message, GameSession gameSession, User user, Game game) {
        if (gameSession.getPlayUser().equals(user)) {
            return List.of(new Response(user,
                    "Ваш опонент загадывает вам слово!\nДождитесь его ответа"));
        }

        if (!Pattern.compile("[а-яё]+").matcher(message).matches()) {
            return List.of(new Response(user,
                    "Слово должно быть на русском языке и не содержать пробелов!"));
        }

        if (game.getWord() == null) {
            game.setWord(message);

            return List.of(new Response(user,
                    "Теперь напишите категорию к которой относится это слово!"));
        }

        game.setCategory(message);
        game.initMultiGame();
        gameSession.setStateSession(StateSession.GAME);

        return List.of(new Response(user,
                        "Слово загадано!"),
                new Response(gameSession.getAnotherUser(user),
                        "Вам загадали слово!"),
                new Response(gameSession.getAnotherUser(user),
                        gameHandler.updateStatsUser("/game", gameSession.getAnotherUser(user), game)));
    }

    /**
     * Обработка многопользовательской сессии в состоянии {@link StateSession#GAME}
     * @return список сообщений, которые адресуются пользователям, подключённым к сессии
     */
    private List<Response> handleGameState(String message, GameSession gameSession, User user, Game game) {
        if (gameSession.getDontPlayUser().equals(user)) {
            return List.of(new Response(user,
                    "Cейчас играет другой игрок!"));
        }

        List<Response> responseList = new ArrayList<>();
        String response = gameHandler.updateStatsUser(message, user, game);

        responseList.add(new Response(user,
                response));
        responseList.add(new Response(gameSession.getAnotherUser(user),
                user.getName() + ": " + message));
        responseList.add(new Response(gameSession.getAnotherUser(user),
                response));

        if (game.getStatusGame().equals(StatusGame.LOSEGAME)
                || game.getStatusGame().equals(StatusGame.WINGAME)) {
            if (!gameSession.nextPlayGame()) {
                gameSessionRepository.delete(gameSession);

                responseList.add(new Response(user,
                        "Игра закончена!"));
                responseList.add(new Response(gameSession.getAnotherUser(user),
                        "Игра закончена!"));

                return responseList;
            }

            game.setWord(null);
            game.setCategory(null);
            gameSession.setStateSession(StateSession.MAKE_WORD);

            responseList.add(new Response(gameSession.getPlayUser(),
                    "Теперь слово загадывают вам!"));
            responseList.add(new Response(gameSession.getDontPlayUser(),
                    "Теперь ваша очередь загадывать слово!"));
        }

        return responseList;
    }

    /**
     * Обрабатывает комманду /stop
     * @param gameSession останавливаемая сессия
     * @param user пользователь, который останавливает сессию
     * @return список сообщений, которые адресуются пользователям, подключённым к сессии
     */
    private List<Response> stopSession(GameSession gameSession, User user) {
        List<Response> responseList = new ArrayList<>();

        Game game = gameSession.getGame();

        switch (gameSession.getTypeGame()) {
            case SINGLE -> {
                if (!game.getStatusGame().equals(StatusGame.STARTGAME)) {
                    switch (gameSession.getGame().getDifficult()) {
                        case EASY -> user.setTotalGameEasy(user.getTotalGameEasy() + 1);
                        case MEDIUM -> user.setTotalGameMedium(user.getTotalGameMedium() + 1);
                        case HARD -> user.setTotalGameHard(user.getTotalGameHard() + 1);
                    }
                }

                responseList.add(new Response(user,
                        "Игра остановлена!"));
            }
            case MULTI -> {
                if (gameSession.getPlayUser().equals(user)) {
                    if (!game.getStatusGame().equals(StatusGame.STARTGAME)) {
                        switch (gameSession.getGame().getDifficult()) {
                            case EASY -> user.setTotalGameEasy(user.getTotalGameEasy() + 1);
                            case MEDIUM -> user.setTotalGameMedium(user.getTotalGameMedium() + 1);
                            case HARD -> user.setTotalGameHard(user.getTotalGameHard() + 1);
                        }
                    }
                }

                responseList.add(new Response(user,
                        "Игра остановлена!"));
                if (!gameSession.getStateSession().equals(StateSession.WAITING_FOR_PLAYERS))
                    responseList.add(new Response(gameSession.getAnotherUser(user),
                            "Игра остновлена вашим опонентом!"));
            }
        }

        gameSessionRepository.delete(gameSession);

        return responseList;
    }
}