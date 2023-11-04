package org.opp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opp.core.handler.GameHandler;
import org.opp.essence.User;

/**
 * Тесты класса GameHandler
 */
public class GameHandlerTest {
    private GameHandler gameHandlerTest;
    private User user;
    @Before
    public void setUp() {
        gameHandlerTest = new GameHandler();
        user = new User();
        user.setStateGame("костя");
    }

    /**
     * Тест для метода getAnswer на реакцию на некорректный ввод: <br>
     * <b>1)</b> Для слишком длинного сообщения <br>
     * <b>2)</b> Для сообщений состоящих из симлово не русского алфавита <br>
     * <b>3), 4)</b> Для оповещения пользователя о использовании буквы в случае верного(неверного) ответа <br>
     */
    @Test
    public void messageHandlerCorrect_Test() {
        Assert.assertEquals("Загадано слово: _ _ _ _ _\n" +
                "У тебя есть 10 жизней", gameHandlerTest.getAnswer("костя", user));
        Assert.assertEquals("Тебе нужно вывести одну букву на русском языке!", gameHandlerTest.getAnswer("/start", user));
        Assert.assertEquals( "В слове есть только буквы русского алфавита!", gameHandlerTest.getAnswer("F", user));
        Assert.assertEquals("В слове есть только буквы русского алфавита!", gameHandlerTest.getAnswer("+", user));
        Assert.assertEquals("Правильно!. У тебя осталось 10 жизней.\n" +
                "Исключили: " + "\n\n" +
                "_ _ _ _ я", gameHandlerTest.getAnswer("я", user));
        Assert.assertEquals("Ты уже вводил эту букву! Попробуй другую.", gameHandlerTest.getAnswer("я", user));
        Assert.assertEquals("Не угадал! У тебя осталось 9 жизней.\n" +
                "Исключили: н" + "\n\n" +
                "_ _ _ _ я", gameHandlerTest.getAnswer("н", user));
        Assert.assertEquals("Ты уже вводил эту букву! Попробуй другую.", gameHandlerTest.getAnswer("н", user));
    }

    /**
     * Тест для метода getAnswer в случае поражения:
     */
    @Test
    public void messageHandlerLose_Test() {
        Assert.assertEquals("Загадано слово: _ _ _ _ _\n" +
                "У тебя есть 10 жизней", gameHandlerTest.getAnswer("костя", user));
        Assert.assertEquals("Не угадал! У тебя осталось 9 жизней.\n" +
                "Исключили: н\n\n" +
                "_ _ _ _ _", gameHandlerTest.getAnswer("н", user));
        Assert.assertEquals("Не угадал! У тебя осталось 8 жизней.\n" +
                "Исключили: нз\n\n" +
                "_ _ _ _ _", gameHandlerTest.getAnswer("з", user));
        Assert.assertEquals("Не угадал! У тебя осталось 7 жизней.\n" +
                "Исключили: нзп\n\n" +
                "_ _ _ _ _", gameHandlerTest.getAnswer("п", user));
        Assert.assertEquals("Не угадал! У тебя осталось 6 жизней.\n" +
                "Исключили: нзпш\n\n" +
                "_ _ _ _ _", gameHandlerTest.getAnswer("ш", user));
        Assert.assertEquals("Не угадал! У тебя осталось 5 жизней.\n" +
                "Исключили: нзпшу\n\n" +
                "_ _ _ _ _", gameHandlerTest.getAnswer("у", user));
        Assert.assertEquals("Не угадал! У тебя осталось 4 жизней.\n" +
                "Исключили: нзпшуд\n\n" +
                "_ _ _ _ _", gameHandlerTest.getAnswer("д", user));
        Assert.assertEquals("Не угадал! У тебя осталось 3 жизней.\n" +
                "Исключили: нзпшудй\n\n" +
                "_ _ _ _ _", gameHandlerTest.getAnswer("й", user));
        Assert.assertEquals("Не угадал! У тебя осталось 2 жизней.\n" +
                "Исключили: нзпшудйл\n\n" +
                "_ _ _ _ _", gameHandlerTest.getAnswer("л", user));
        Assert.assertEquals("Не угадал! У тебя осталось 1 жизней.\n" +
                "Исключили: нзпшудйлщ\n\n" +
                "_ _ _ _ _", gameHandlerTest.getAnswer("щ", user));
        Assert.assertEquals("Ты проиграл! Загаданое слово: костя!\n" +
                "Если хочешь сыграть ещё раз напиши /game", gameHandlerTest.getAnswer("ч", user));
    }

    /**
     * Тест для метода getAnswer в случае победы:
     */
    @Test
    public void messageHandlerWin_Test() {
        Assert.assertEquals("Загадано слово: _ _ _ _ _\n" +
                "У тебя есть 10 жизней", gameHandlerTest.getAnswer("костя", user));
        Assert.assertEquals("Правильно!. У тебя осталось 10 жизней.\n" +
                "Исключили: \n\n" +
                "_ _ _ _ я", gameHandlerTest.getAnswer("я", user));
        Assert.assertEquals("Правильно!. У тебя осталось 10 жизней.\n" +
                "Исключили: \n\n" +
                "к _ _ _ я", gameHandlerTest.getAnswer("к", user));
        Assert.assertEquals("Правильно!. У тебя осталось 10 жизней.\n" +
                "Исключили: \n\n" +
                "к о _ _ я", gameHandlerTest.getAnswer("о", user));
        Assert.assertEquals("Правильно!. У тебя осталось 10 жизней.\n" +
                "Исключили: \n\n" +
                "к о с _ я", gameHandlerTest.getAnswer("с", user));
        Assert.assertEquals("Молодец! Ты отгадал слово: костя!\n" +
                "Если хочешь сыграть ещё раз напиши /game", gameHandlerTest.getAnswer("т", user));
    }
}