package org.opp.core.handler;

import org.opp.data.models.Game;
import org.opp.data.models.User;
import org.opp.data.models.types.Difficult;
import org.opp.data.models.types.State;
import org.opp.data.models.types.StatusGame;
import org.opp.data.repositories.WordRepository;

import java.util.regex.Pattern;

/**
 * Класс, отвечающий за процесс игры
 */
public class GameHandler {
    private WordRepository wordRepository;

    public GameHandler() {
        this.wordRepository = new WordRepository();
    }

    /**
     * Сеттер, используемый для назначения WordRepository с необходимыми свойствами.
     * @param wordRepository замоканный объект класса WordRepository
     */
    public void setWordRepository(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    /**
     * Изменяет состояние User в зависимости от событий в игре.
     * @param message сообщение пользователя
     * @param user объект содержащий данные пользователя и игру, принадлежащую данному пользователю
     * @return ответ на сообщение пользователя
     */
    public String updateStateUser(String message, User user) {
        if (user.getState().equals(State.IDLE)) {
            user.setStateGame(wordRepository.getRandomWord());
        }

        String response = getResponse(message, user.getUserGame());

        if(user.getStatusGame().equals(StatusGame.LOSEGAME)) {
            switch (user.getDifficultGame()) {
                case EASY -> user.setTotalGameEasy(user.getTotalGameEasy() + 1);
                case MEDIUM -> user.setTotalGameMedium(user.getTotalGameMedium() + 1);
                case HARD -> user.setTotalGameHard(user.getTotalGameHard() + 1);
            }
            user.setStateIdle();
        }
        else if (user.getStatusGame().equals(StatusGame.WINGAME)) {
            switch (user.getDifficultGame()) {
                case EASY ->  {
                    user.setTotalGameEasy(user.getTotalGameEasy() + 1);
                    user.setTotalWinEasy(user.getTotalWinEasy() + 1);
                }
                case MEDIUM -> {
                    user.setTotalGameMedium(user.getTotalGameMedium() + 1);
                    user.setTotalWinMedium(user.getTotalWinMedium() + 1);
                }
                case HARD -> {
                    user.setTotalGameHard(user.getTotalGameHard() + 1);
                    user.setTotalWinHard(user.getTotalWinHard() + 1);
                }
            }
            user.setStateIdle();
        }

        return response;
    }


    /**
     * Реализует логику игры. Определяет ответ пользователю на его сообщение в зависимости от состояния игры.
     * @param message сообщение пользователя
     * @param game содержит данные о игре пользователя
     * @return ответ на сообщение пользователя
     */
    public String getResponse(String message, Game game) {
        if (game.getStatusGame().equals(StatusGame.STARTGAME)) {
            switch (message) {
                case "/game" -> {
                    return """
                            Выбери сложность:
                            /easy - 15 попыток
                            /medium - 10 попыток
                            /hard - 5 попыток
                            """;
                }
                case "/easy" -> {
                    game.setDifficult(Difficult.EASY);
                    game.setNumberOfLives(15);
                }
                case "/medium" -> {
                    game.setDifficult(Difficult.MEDIUM);
                    game.setNumberOfLives(10);
                }
                case "/hard" -> {
                    game.setDifficult(Difficult.HARD);
                    game.setNumberOfLives(5);
                }
                default -> {
                    return """
                            Тебе нужно выбрать сложность!
                            Если хочешь остановить игру /stop""";
                }
            }

            game.setStatusGame(StatusGame.GAME);
            return "Загадано слово: " + game.getGameViewOfTheWord() + "\n" +
                    "Тема: " + game.getCategory() + "\n" +
                    "Количество попыток: " + game.getNumberOfLives();
        }
        if (message.length() > 1) {
            return "Тебе нужно вывести одну букву на русском языке!";
        }
        if (!Pattern.compile("[а-я]").matcher(message).matches() && message.charAt(0) != 'ё') {
            return "В слове есть только буквы русского алфавита!";
        }
        if (game.getGameViewOfTheWord().contains(message) ||
                game.getWordFromExcludedLetters().contains(message)) {
            return "Ты уже вводил эту букву! Попробуй другую.";
        }

        if (game.getWord().contains(message)) {
            String word = game.getWord();


            for (int i = 0; i < word.length(); i++) {
                if (word.charAt(i) == message.charAt(0)) {
                    game.setGameViewOfTheWord(game.getGameViewOfTheWord().substring(0, i * 2) + message.charAt(0) +
                            game.getGameViewOfTheWord().substring(i * 2 + 1));
                }
            }

            if (!game.getGameViewOfTheWord().contains("_")) {
                game.setStatusGame(StatusGame.WINGAME);
                return "Молодец! Ты отгадал слово: " + game.getWord() + "!\n" +
                        "Если хочешь сыграть ещё раз напиши /game";
            }
            return "Правильно!. Осталось попыток: " + game.getNumberOfLives() + "\n" +
                    "Исключили: " + game.getWordFromExcludedLetters() + "\n\n" +
                    "Тема: " + game.getCategory() + "\n" +
                    game.getGameViewOfTheWord();
        }
        else {
            game.setNumberOfLives(game.getNumberOfLives() - 1);
            game.setWordFromExcludedLetters(game.getWordFromExcludedLetters() + message.charAt(0));

            if (game.getNumberOfLives() <= 0) {
                game.setStatusGame(StatusGame.LOSEGAME);
                return "Ты проиграл! Загаданое слово: " + game.getWord() + "!\n" +
                        "Если хочешь сыграть ещё раз напиши /game";
            }
            return "Не угадал! Осталось попыток: " + game.getNumberOfLives() + "\n" +
                    "Исключили: " + game.getWordFromExcludedLetters() + "\n\n" +
                    "Тема: " + game.getCategory() + "\n" +
                    game.getGameViewOfTheWord();
        }
    }
}
