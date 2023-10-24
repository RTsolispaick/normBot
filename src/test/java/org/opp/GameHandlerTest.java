package org.opp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opp.core.handler.GameHandler;
import org.opp.essence.User;

/**
 * Тесты класса GameHandler
 */
public class GameHandlerTest {
    private GameHandler gameHandlerTest;
    private User user;
    @BeforeEach
    public void setUp() {
        gameHandlerTest = new GameHandler();
        user = new User();
        user.setStateGame();
    }

    /**
     * Тест для метода getAnswer на реакцию на некорректный ввод: <br>
     * <b>1)</b> Для слишком длинного сообщения <br>
     * <b>2)</b> Для сообщений состоящих из симлово не русского алфавита <br>
     * <b>3), 4)</b> Для оповещения пользователя о использовании буквы в случае верного(неверного) ответа <br>
     */
    @Test
    public void messageHandlerCorrect_Test() {
        Assertions.assertEquals("Загадано слово: _ _ _ _ _\n" +
                "У тебя есть 10 жизней", gameHandlerTest.startGame("костя", user));
        Assertions.assertEquals("Тебе нужно вывести одну букву на русском языке!", gameHandlerTest.getAnswer("/start", user));
        Assertions.assertEquals( "В слове есть только буквы русского алфавита!", gameHandlerTest.getAnswer("F", user));
        Assertions.assertEquals("В слове есть только буквы русского алфавита!", gameHandlerTest.getAnswer("+", user));
        Assertions.assertEquals("Правильно!. У тебя осталось 10 жизней.\n" +
                "Исключили: " + "\n\n" +
                "_ _ _ _ я", gameHandlerTest.getAnswer("я", user));
        Assertions.assertEquals("Ты уже вводил эту букву! Попробуй другую.", gameHandlerTest.getAnswer("я", user));
        Assertions.assertEquals("Не угадал! У тебя осталось 9 жизней.\n" +
                "Исключили: н" + "\n\n" +
                "_ _ _ _ я", gameHandlerTest.getAnswer("н", user));
        Assertions.assertEquals("Ты уже вводил эту букву! Попробуй другую.", gameHandlerTest.getAnswer("н", user));
    }

    /**
     * Тест для метода getAnswer в случае поражения:
     */
    @Test
    public void messageHandlerLose_Test() {
        Assertions.assertEquals("Загадано слово: _ _ _ _ _\n" +
                "У тебя есть 10 жизней", gameHandlerTest.startGame("костя", user));
        Assertions.assertEquals("Не угадал! У тебя осталось 9 жизней.\n" +
                "Исключили: н\n\n" +
                "_ _ _ _ _", gameHandlerTest.getAnswer("н", user));
        Assertions.assertEquals("Не угадал! У тебя осталось 8 жизней.\n" +
                "Исключили: нз\n\n" +
                "_ _ _ _ _", gameHandlerTest.getAnswer("з", user));
        Assertions.assertEquals("Не угадал! У тебя осталось 7 жизней.\n" +
                "Исключили: нзп\n\n" +
                "_ _ _ _ _", gameHandlerTest.getAnswer("п", user));
        Assertions.assertEquals("Не угадал! У тебя осталось 6 жизней.\n" +
                "Исключили: нзпш\n\n" +
                "_ _ _ _ _", gameHandlerTest.getAnswer("ш", user));
        Assertions.assertEquals("Не угадал! У тебя осталось 5 жизней.\n" +
                "Исключили: нзпшу\n\n" +
                "_ _ _ _ _", gameHandlerTest.getAnswer("у", user));
        Assertions.assertEquals("Не угадал! У тебя осталось 4 жизней.\n" +
                "Исключили: нзпшуд\n\n" +
                "_ _ _ _ _", gameHandlerTest.getAnswer("д", user));
        Assertions.assertEquals("Не угадал! У тебя осталось 3 жизней.\n" +
                "Исключили: нзпшудй\n\n" +
                "_ _ _ _ _", gameHandlerTest.getAnswer("й", user));
        Assertions.assertEquals("Не угадал! У тебя осталось 2 жизней.\n" +
                "Исключили: нзпшудйл\n\n" +
                "_ _ _ _ _", gameHandlerTest.getAnswer("л", user));
        Assertions.assertEquals("Не угадал! У тебя осталось 1 жизней.\n" +
                "Исключили: нзпшудйлщ\n\n" +
                "_ _ _ _ _", gameHandlerTest.getAnswer("щ", user));
        Assertions.assertEquals("Ты проиграл! Загаданое слово: костя!\n" +
                "Если хочешь сыграть ещё раз напиши /game", gameHandlerTest.getAnswer("ч", user));
    }

    /**
     * Тест для метода getAnswer в случае победы:
     */
    @Test
    public void messageHandlerWin_Test() {
        Assertions.assertEquals("Загадано слово: _ _ _ _ _\n" +
                "У тебя есть 10 жизней", gameHandlerTest.startGame("костя", user));
        Assertions.assertEquals("Правильно!. У тебя осталось 10 жизней.\n" +
                "Исключили: \n\n" +
                "_ _ _ _ я", gameHandlerTest.getAnswer("я", user));
        Assertions.assertEquals("Правильно!. У тебя осталось 10 жизней.\n" +
                "Исключили: \n\n" +
                "к _ _ _ я", gameHandlerTest.getAnswer("к", user));
        Assertions.assertEquals("Правильно!. У тебя осталось 10 жизней.\n" +
                "Исключили: \n\n" +
                "к о _ _ я", gameHandlerTest.getAnswer("о", user));
        Assertions.assertEquals("Правильно!. У тебя осталось 10 жизней.\n" +
                "Исключили: \n\n" +
                "к о с _ я", gameHandlerTest.getAnswer("с", user));
        Assertions.assertEquals("Молодец! Ты отгадал слово: костя!\n" +
                "Если хочешь сыграть ещё раз напиши /game", gameHandlerTest.getAnswer("т", user));
    }
}