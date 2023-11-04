package org.opp.core.handler;

import org.opp.essence.User;

import java.util.regex.Pattern;

/**
 *
 * Класс GameHandler реализует процесс игры: <br>
 * 1) Проверять на корректность запросы пользователя. <br>*
 * 2) В случае корректного запроса генерирует ответ и изменяет состояние игры.
 */
public class GameHandler {
    /**
     * Обрабатываем ответ пользоваетеля
     * @param message ответ пользователя
     * @param user объект с данными, необходимыми для обработки ответа
     * @return возвращаем ответ на запрос пользователя
     */
    public String getAnswer(String message, User user) {
        if (user.getStatusGame() == -1) {
            user.setStatusGame(1);
            return "Загадано слово: " + user.getGameViewOfTheWord() + "\n" +
                    "У тебя есть " + user.getNumberOfLives() + " жизней";
        }
        if (message.length() > 1) {
            return "Тебе нужно вывести одну букву на русском языке!";
        }
        if (!Pattern.compile("[а-я]").matcher(message).matches() && message.charAt(0) != 'ё') {
            return "В слове есть только буквы русского алфавита!";
        }
        if (user.getGameViewOfTheWord().indexOf(message) != -1 ||
                user.getWordFromExcludedLetters().indexOf(message) != -1) {
            return "Ты уже вводил эту букву! Попробуй другую.";
        }

        if (user.getWord().contains(message)) {
            String word = user.getWord();

            for (int i = 0; i < word.length(); i++) {
                if (word.charAt(i) == message.charAt(0)) {
                    user.getGameViewOfTheWord().setCharAt(i * 2, message.charAt(0));
                }
            }

            if (user.getGameViewOfTheWord().indexOf("_") == -1)
                return "Молодец! Ты отгадал слово: " + user.getWord() + "!\n" +
                        "Если хочешь сыграть ещё раз напиши /game";
            return "Правильно!. У тебя осталось " + user.getNumberOfLives() + " жизней.\n" +
                    "Исключили: " + user.getWordFromExcludedLetters() + "\n\n" +
                    user.getGameViewOfTheWord();
        }
        else {
            user.setNumberOfLives(user.getNumberOfLives() - 1);
            user.getWordFromExcludedLetters().append(message.charAt(0));

            if (user.getNumberOfLives() == 0)
                return "Ты проиграл! Загаданое слово: " + user.getWord() + "!\n" +
                        "Если хочешь сыграть ещё раз напиши /game";
            return "Не угадал! У тебя осталось " + user.getNumberOfLives() + " жизней.\n" +
                    "Исключили: " + user.getWordFromExcludedLetters() + "\n\n" +
                    user.getGameViewOfTheWord();
        }
    }
}