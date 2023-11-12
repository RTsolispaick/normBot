package org.opp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opp.core.handler.GameHandler;
import org.opp.data.models.User;
import org.opp.data.models.Word;

/**
 * Тест класса GameHandler
 */
public class GameHandlerTest {
    private GameHandler gameHandlerTest;
    private User user;

    @Before
    public void setUp() {
        gameHandlerTest = new GameHandler();
        user = new User(0L, "user");
        user.setStateGame(new Word("теннис", "спорт"));
    }

    /**
     * Тест обработки некорректного ввода: <br>
     * 1) Выбора сложности <br>
     * 2) Процесс игры <br>
     * <br>
     * В этом тесте находимся в сложности /easy
     */
    @Test
    public void messageHandlerCorrect_Test() {
        Assert.assertEquals("""
                            Тебе нужно выбрать сложность!
                            Если хочешь остановить игру /stop""", gameHandlerTest.updateStateUser("uncorrect input", user));
        Assert.assertEquals("""
                Загадано слово: _ _ _ _ _ _
                Тема: спорт
                Количество попыток: 15""", gameHandlerTest.updateStateUser("/easy", user));
        Assert.assertEquals("Тебе нужно вывести одну букву на русском языке!", gameHandlerTest.updateStateUser("/start", user));
        Assert.assertEquals( "В слове есть только буквы русского алфавита!", gameHandlerTest.updateStateUser("F", user));
        Assert.assertEquals("В слове есть только буквы русского алфавита!", gameHandlerTest.updateStateUser("+", user));
        Assert.assertEquals("""
                Правильно!. Осталось попыток: 15
                Исключили:\s

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStateUser("т", user));
        Assert.assertEquals("Ты уже вводил эту букву! Попробуй другую.", gameHandlerTest.updateStateUser("т", user));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 14
                Исключили: я

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStateUser("я", user));
        Assert.assertEquals("Ты уже вводил эту букву! Попробуй другую.", gameHandlerTest.updateStateUser("я", user));
    }

    /**
     * Тест поражения <br>
     * <br>
     * Находимся в сложности /hard
     */
    @Test
    public void messageHandlerLose_Test() {
        Integer totalWinGameBefore = user.getTotalWinHard();
        Integer totalGameBefore = user.getTotalGameHard() + 1;

        Assert.assertEquals("""
                Загадано слово: _ _ _ _ _ _
                Тема: спорт
                Количество попыток: 5""", gameHandlerTest.updateStateUser("/hard", user));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 4
                Исключили: я

                Тема: спорт
                _ _ _ _ _ _""", gameHandlerTest.updateStateUser("я", user));
        Assert.assertEquals("""
                Правильно!. Осталось попыток: 4
                Исключили: я

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStateUser("т", user));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 3
                Исключили: яп

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStateUser("п", user));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 2
                Исключили: япш

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStateUser("ш", user));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 1
                Исключили: япшу

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStateUser("у", user));
        Assert.assertEquals("Ты проиграл! Загаданое слово: теннис!\n" +
                "Если хочешь сыграть ещё раз напиши /game", gameHandlerTest.updateStateUser("д", user));

        Assert.assertEquals(totalGameBefore, user.getTotalGameHard());
        Assert.assertEquals(totalWinGameBefore, user.getTotalWinHard());
    }

    /**
     * Тест победы <br>
     * <br>
     * Находимся в сложности /medium
     */
    @Test
    public void messageHandlerWin_Test() {
        Integer totalWinGameBefore = user.getTotalWinMedium() + 1;
        Integer totalGameBefore = user.getTotalGameMedium() + 1;

        Assert.assertEquals("""
                Загадано слово: _ _ _ _ _ _
                Тема: спорт
                Количество попыток: 10""", gameHandlerTest.updateStateUser("/medium", user));
        Assert.assertEquals("""
                Правильно!. Осталось попыток: 10
                Исключили:\s

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStateUser("т", user));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 9
                Исключили: п

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStateUser("п", user));
        Assert.assertEquals("""
                Правильно!. Осталось попыток: 9
                Исключили: п

                Тема: спорт
                т _ н н _ _""", gameHandlerTest.updateStateUser("н", user));
        Assert.assertEquals("""
                Правильно!. Осталось попыток: 9
                Исключили: п

                Тема: спорт
                т _ н н _ с""", gameHandlerTest.updateStateUser("с", user));
        Assert.assertEquals("""
                Правильно!. Осталось попыток: 9
                Исключили: п

                Тема: спорт
                т е н н _ с""", gameHandlerTest.updateStateUser("е", user));
        Assert.assertEquals("Молодец! Ты отгадал слово: теннис!\n" +
                "Если хочешь сыграть ещё раз напиши /game", gameHandlerTest.updateStateUser("и", user));

        Assert.assertEquals(totalGameBefore, user.getTotalGameMedium());
        Assert.assertEquals(totalWinGameBefore, user.getTotalWinMedium());
    }
}