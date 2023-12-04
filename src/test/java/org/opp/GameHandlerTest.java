package org.opp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opp.core.handler.GameHandler;
import org.opp.data.models.Game;
import org.opp.data.models.User;
import org.opp.data.models.Word;
import org.opp.data.models.types.Platform;

/**
 * Тест класса GameHandler
 */
public class GameHandlerTest {
    private final GameHandler gameHandlerTest = new GameHandler();
    private final Game testGame = new Game();
    private final User user = new User(Platform.TG,0L, "user");

    @Before
    public void SetUp() {
        testGame.initSingleGame(new Word("теннис", "спорт"));
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
                            Выбери сложность:
                            /easy - 15 попыток
                            /medium - 10 попыток
                            /hard - 5 попыток
                            """, gameHandlerTest.updateStatsUser("/game", user, testGame));
        Assert.assertEquals("""
                            Тебе нужно выбрать сложность!
                            Если хочешь остановить игру /stop""", gameHandlerTest.updateStatsUser("uncorrect input", user, testGame));
        Assert.assertEquals("""
                Загадано слово: _ _ _ _ _ _
                Тема: спорт
                Количество попыток: 15""", gameHandlerTest.updateStatsUser("/easy", user, testGame));
        Assert.assertEquals("Тебе нужно вывести одну букву на русском языке!", gameHandlerTest.updateStatsUser("/start", user, testGame));
        Assert.assertEquals( "В слове есть только буквы русского алфавита!", gameHandlerTest.updateStatsUser("F", user, testGame));
        Assert.assertEquals("В слове есть только буквы русского алфавита!", gameHandlerTest.updateStatsUser("+", user, testGame));
        Assert.assertEquals("""
                Правильно!. Осталось попыток: 15
                Исключили:\s

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStatsUser("т", user, testGame));
        Assert.assertEquals("Ты уже вводил эту букву! Попробуй другую.", gameHandlerTest.updateStatsUser("т", user, testGame));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 14
                Исключили: я

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStatsUser("я", user, testGame));
        Assert.assertEquals("Ты уже вводил эту букву! Попробуй другую.", gameHandlerTest.updateStatsUser("я", user, testGame));
    }

    /**
     * Тест поражения в сложности hard
     */
    @Test
    public void messageHandlerLoseHard_Test() {
        Integer totalWinGameBefore = user.getTotalWinHard();
        Integer totalGameBefore = user.getTotalGameHard() + 1;

        Assert.assertEquals("""
                            Выбери сложность:
                            /easy - 15 попыток
                            /medium - 10 попыток
                            /hard - 5 попыток
                            """, gameHandlerTest.updateStatsUser("/game", user, testGame));
        Assert.assertEquals("""
                Загадано слово: _ _ _ _ _ _
                Тема: спорт
                Количество попыток: 5""", gameHandlerTest.updateStatsUser("/hard", user, testGame));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 4
                Исключили: я

                Тема: спорт
                _ _ _ _ _ _""", gameHandlerTest.updateStatsUser("я", user, testGame));
        Assert.assertEquals("""
                Правильно!. Осталось попыток: 4
                Исключили: я

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStatsUser("т", user, testGame));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 3
                Исключили: яп

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStatsUser("п", user, testGame));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 2
                Исключили: япш

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStatsUser("ш", user, testGame));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 1
                Исключили: япшу

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStatsUser("у", user, testGame));
        Assert.assertEquals("Ты проиграл! Загаданое слово: теннис!", gameHandlerTest.updateStatsUser("д", user, testGame));

        Assert.assertEquals(totalGameBefore, user.getTotalGameHard());
        Assert.assertEquals(totalWinGameBefore, user.getTotalWinHard());
    }

    /**
     * Тест победы в сложности hard
     */
    @Test
    public void messageHandlerWinHard_Test() {
        Integer totalWinGameBefore = user.getTotalWinHard() + 1;
        Integer totalGameBefore = user.getTotalGameHard() + 1;

        Assert.assertEquals("""
                            Выбери сложность:
                            /easy - 15 попыток
                            /medium - 10 попыток
                            /hard - 5 попыток
                            """, gameHandlerTest.updateStatsUser("/game", user, testGame));
        Assert.assertEquals("""
                Загадано слово: _ _ _ _ _ _
                Тема: спорт
                Количество попыток: 5""", gameHandlerTest.updateStatsUser("/hard", user, testGame));
        Assert.assertEquals("""
                Правильно!. Осталось попыток: 5
                Исключили:\s

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStatsUser("т", user, testGame));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 4
                Исключили: п

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStatsUser("п", user, testGame));
        Assert.assertEquals("""
                Правильно!. Осталось попыток: 4
                Исключили: п

                Тема: спорт
                т _ н н _ _""", gameHandlerTest.updateStatsUser("н", user, testGame));
        Assert.assertEquals("""
                Правильно!. Осталось попыток: 4
                Исключили: п

                Тема: спорт
                т _ н н _ с""", gameHandlerTest.updateStatsUser("с", user, testGame));
        Assert.assertEquals("""
                Правильно!. Осталось попыток: 4
                Исключили: п

                Тема: спорт
                т е н н _ с""", gameHandlerTest.updateStatsUser("е", user, testGame));
        Assert.assertEquals("Молодец! Ты отгадал слово: теннис!"
                , gameHandlerTest.updateStatsUser("и", user, testGame));

        Assert.assertEquals(totalGameBefore, user.getTotalGameHard());
        Assert.assertEquals(totalWinGameBefore, user.getTotalWinHard());
    }

    /**
     * Тест поражения в /medium
     */
    @Test
    public void messageHandlerLoseMedium_Test() {
        Integer totalWinGameBefore = user.getTotalWinMedium();
        Integer totalGameBefore = user.getTotalGameMedium() + 1;

        Assert.assertEquals("""
                            Выбери сложность:
                            /easy - 15 попыток
                            /medium - 10 попыток
                            /hard - 5 попыток
                            """, gameHandlerTest.updateStatsUser("/game", user, testGame));
        Assert.assertEquals("""
                Загадано слово: _ _ _ _ _ _
                Тема: спорт
                Количество попыток: 10""", gameHandlerTest.updateStatsUser("/medium", user, testGame));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 9
                Исключили: я

                Тема: спорт
                _ _ _ _ _ _""", gameHandlerTest.updateStatsUser("я", user, testGame));
        Assert.assertEquals("""
                Правильно!. Осталось попыток: 9
                Исключили: я

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStatsUser("т", user, testGame));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 8
                Исключили: яп

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStatsUser("п", user, testGame));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 7
                Исключили: япш

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStatsUser("ш", user, testGame));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 6
                Исключили: япшу

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStatsUser("у", user, testGame));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 5
                Исключили: япшуй

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStatsUser("й", user, testGame));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 4
                Исключили: япшуйк

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStatsUser("к", user, testGame));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 3
                Исключили: япшуйкц

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStatsUser("ц", user, testGame));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 2
                Исключили: япшуйкцг

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStatsUser("г", user, testGame));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 1
                Исключили: япшуйкцгщ

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStatsUser("щ", user, testGame));
        Assert.assertEquals("Ты проиграл! Загаданое слово: теннис!"
                , gameHandlerTest.updateStatsUser("д", user, testGame));

        Assert.assertEquals(totalGameBefore, user.getTotalGameMedium());
        Assert.assertEquals(totalWinGameBefore, user.getTotalWinMedium());
    }

    /**
     * Тест победы в сложности medium
     */
    @Test
    public void messageHandlerWinMedium_Test() {
        Integer totalWinGameBefore = user.getTotalWinMedium() + 1;
        Integer totalGameBefore = user.getTotalGameMedium() + 1;

        Assert.assertEquals("""
                            Выбери сложность:
                            /easy - 15 попыток
                            /medium - 10 попыток
                            /hard - 5 попыток
                            """, gameHandlerTest.updateStatsUser("/game", user, testGame));
        Assert.assertEquals("""
                Загадано слово: _ _ _ _ _ _
                Тема: спорт
                Количество попыток: 10""", gameHandlerTest.updateStatsUser("/medium", user, testGame));
        Assert.assertEquals("""
                Правильно!. Осталось попыток: 10
                Исключили:\s

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStatsUser("т", user, testGame));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 9
                Исключили: п

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStatsUser("п", user, testGame));
        Assert.assertEquals("""
                Правильно!. Осталось попыток: 9
                Исключили: п

                Тема: спорт
                т _ н н _ _""", gameHandlerTest.updateStatsUser("н", user, testGame));
        Assert.assertEquals("""
                Правильно!. Осталось попыток: 9
                Исключили: п

                Тема: спорт
                т _ н н _ с""", gameHandlerTest.updateStatsUser("с", user, testGame));
        Assert.assertEquals("""
                Правильно!. Осталось попыток: 9
                Исключили: п

                Тема: спорт
                т е н н _ с""", gameHandlerTest.updateStatsUser("е", user, testGame));
        Assert.assertEquals("Молодец! Ты отгадал слово: теннис!"
                , gameHandlerTest.updateStatsUser("и", user, testGame));

        Assert.assertEquals(totalGameBefore, user.getTotalGameMedium());
        Assert.assertEquals(totalWinGameBefore, user.getTotalWinMedium());
    }

    /**
     * Тест поражения в сложности easy
     */
    @Test
    public void messageHandlerLoseEasy_Test() {
        Integer totalWinGameBefore = user.getTotalWinEasy();
        Integer totalGameBefore = user.getTotalGameEasy() + 1;

        Assert.assertEquals("""
                            Выбери сложность:
                            /easy - 15 попыток
                            /medium - 10 попыток
                            /hard - 5 попыток
                            """, gameHandlerTest.updateStatsUser("/game", user, testGame));
        Assert.assertEquals("""
                Загадано слово: _ _ _ _ _ _
                Тема: спорт
                Количество попыток: 15""", gameHandlerTest.updateStatsUser("/easy", user, testGame));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 14
                Исключили: я

                Тема: спорт
                _ _ _ _ _ _""", gameHandlerTest.updateStatsUser("я", user, testGame));
        Assert.assertEquals("""
                Правильно!. Осталось попыток: 14
                Исключили: я

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStatsUser("т", user, testGame));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 13
                Исключили: яп

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStatsUser("п", user, testGame));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 12
                Исключили: япш

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStatsUser("ш", user, testGame));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 11
                Исключили: япшу

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStatsUser("у", user, testGame));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 10
                Исключили: япшуй

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStatsUser("й", user, testGame));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 9
                Исключили: япшуйк

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStatsUser("к", user, testGame));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 8
                Исключили: япшуйкц

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStatsUser("ц", user, testGame));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 7
                Исключили: япшуйкцг

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStatsUser("г", user, testGame));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 6
                Исключили: япшуйкцгщ

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStatsUser("щ", user, testGame));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 5
                Исключили: япшуйкцгщз

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStatsUser("з", user, testGame));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 4
                Исключили: япшуйкцгщзх

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStatsUser("х", user, testGame));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 3
                Исключили: япшуйкцгщзхф

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStatsUser("ф", user, testGame));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 2
                Исключили: япшуйкцгщзхфъ

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStatsUser("ъ", user, testGame));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 1
                Исключили: япшуйкцгщзхфъч

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStatsUser("ч", user, testGame));
        Assert.assertEquals("Ты проиграл! Загаданое слово: теннис!"
                , gameHandlerTest.updateStatsUser("д", user, testGame));

        Assert.assertEquals(totalGameBefore, user.getTotalGameEasy());
        Assert.assertEquals(totalWinGameBefore, user.getTotalWinEasy());
    }

    /**
     * Тест победы в сложности medium
     */
    @Test
    public void messageHandlerWinEasy_Test() {
        Integer totalWinGameBefore = user.getTotalWinEasy() + 1;
        Integer totalGameBefore = user.getTotalGameEasy() + 1;

        Assert.assertEquals("""
                            Выбери сложность:
                            /easy - 15 попыток
                            /medium - 10 попыток
                            /hard - 5 попыток
                            """, gameHandlerTest.updateStatsUser("/game", user, testGame));
        Assert.assertEquals("""
                Загадано слово: _ _ _ _ _ _
                Тема: спорт
                Количество попыток: 15""", gameHandlerTest.updateStatsUser("/easy", user, testGame));
        Assert.assertEquals("""
                Правильно!. Осталось попыток: 15
                Исключили:\s

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStatsUser("т", user, testGame));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 14
                Исключили: п

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStatsUser("п", user, testGame));
        Assert.assertEquals("""
                Правильно!. Осталось попыток: 14
                Исключили: п

                Тема: спорт
                т _ н н _ _""", gameHandlerTest.updateStatsUser("н", user, testGame));
        Assert.assertEquals("""
                Правильно!. Осталось попыток: 14
                Исключили: п

                Тема: спорт
                т _ н н _ с""", gameHandlerTest.updateStatsUser("с", user, testGame));
        Assert.assertEquals("""
                Правильно!. Осталось попыток: 14
                Исключили: п

                Тема: спорт
                т е н н _ с""", gameHandlerTest.updateStatsUser("е", user, testGame));
        Assert.assertEquals("Молодец! Ты отгадал слово: теннис!"
                , gameHandlerTest.updateStatsUser("и", user, testGame));

        Assert.assertEquals(totalGameBefore, user.getTotalGameEasy());
        Assert.assertEquals(totalWinGameBefore, user.getTotalWinEasy());
    }
}
