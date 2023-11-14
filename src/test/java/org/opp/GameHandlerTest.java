package org.opp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.opp.core.handler.GameHandler;
import org.opp.data.models.User;
import org.opp.data.models.Word;
import org.opp.data.repositories.WordRepository;

/**
 * Тест класса GameHandler
 */
public class GameHandlerTest {
    private final WordRepository wordRepositoryTest = Mockito.mock(WordRepository.class);
    private final GameHandler gameHandlerTest = new GameHandler(wordRepositoryTest);
    private final User user = new User(0L, "user");

    @Before
    public void SetUp(){
        Mockito.when(wordRepositoryTest.getRandomWord()).thenReturn(new Word("теннис", "спорт"));
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
                            """, gameHandlerTest.updateStateUser("/game", user));
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
                            """, gameHandlerTest.updateStateUser("/game", user));
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
                            """, gameHandlerTest.updateStateUser("/game", user));
        Assert.assertEquals("""
                Загадано слово: _ _ _ _ _ _
                Тема: спорт
                Количество попыток: 5""", gameHandlerTest.updateStateUser("/hard", user));
        Assert.assertEquals("""
                Правильно!. Осталось попыток: 5
                Исключили:\s

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStateUser("т", user));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 4
                Исключили: п

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStateUser("п", user));
        Assert.assertEquals("""
                Правильно!. Осталось попыток: 4
                Исключили: п

                Тема: спорт
                т _ н н _ _""", gameHandlerTest.updateStateUser("н", user));
        Assert.assertEquals("""
                Правильно!. Осталось попыток: 4
                Исключили: п

                Тема: спорт
                т _ н н _ с""", gameHandlerTest.updateStateUser("с", user));
        Assert.assertEquals("""
                Правильно!. Осталось попыток: 4
                Исключили: п

                Тема: спорт
                т е н н _ с""", gameHandlerTest.updateStateUser("е", user));
        Assert.assertEquals("Молодец! Ты отгадал слово: теннис!\n" +
                "Если хочешь сыграть ещё раз напиши /game", gameHandlerTest.updateStateUser("и", user));

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
                            """, gameHandlerTest.updateStateUser("/game", user));
        Assert.assertEquals("""
                Загадано слово: _ _ _ _ _ _
                Тема: спорт
                Количество попыток: 10""", gameHandlerTest.updateStateUser("/medium", user));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 9
                Исключили: я

                Тема: спорт
                _ _ _ _ _ _""", gameHandlerTest.updateStateUser("я", user));
        Assert.assertEquals("""
                Правильно!. Осталось попыток: 9
                Исключили: я

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStateUser("т", user));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 8
                Исключили: яп

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStateUser("п", user));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 7
                Исключили: япш

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStateUser("ш", user));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 6
                Исключили: япшу

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStateUser("у", user));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 5
                Исключили: япшуй

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStateUser("й", user));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 4
                Исключили: япшуйк

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStateUser("к", user));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 3
                Исключили: япшуйкц

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStateUser("ц", user));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 2
                Исключили: япшуйкцг

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStateUser("г", user));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 1
                Исключили: япшуйкцгщ

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStateUser("щ", user));
        Assert.assertEquals("Ты проиграл! Загаданое слово: теннис!\n" +
                "Если хочешь сыграть ещё раз напиши /game", gameHandlerTest.updateStateUser("д", user));

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
                            """, gameHandlerTest.updateStateUser("/game", user));
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
                            """, gameHandlerTest.updateStateUser("/game", user));
        Assert.assertEquals("""
                Загадано слово: _ _ _ _ _ _
                Тема: спорт
                Количество попыток: 15""", gameHandlerTest.updateStateUser("/easy", user));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 14
                Исключили: я

                Тема: спорт
                _ _ _ _ _ _""", gameHandlerTest.updateStateUser("я", user));
        Assert.assertEquals("""
                Правильно!. Осталось попыток: 14
                Исключили: я

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStateUser("т", user));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 13
                Исключили: яп

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStateUser("п", user));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 12
                Исключили: япш

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStateUser("ш", user));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 11
                Исключили: япшу

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStateUser("у", user));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 10
                Исключили: япшуй

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStateUser("й", user));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 9
                Исключили: япшуйк

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStateUser("к", user));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 8
                Исключили: япшуйкц

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStateUser("ц", user));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 7
                Исключили: япшуйкцг

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStateUser("г", user));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 6
                Исключили: япшуйкцгщ

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStateUser("щ", user));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 5
                Исключили: япшуйкцгщз

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStateUser("з", user));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 4
                Исключили: япшуйкцгщзх

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStateUser("х", user));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 3
                Исключили: япшуйкцгщзхф

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStateUser("ф", user));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 2
                Исключили: япшуйкцгщзхфъ

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStateUser("ъ", user));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 1
                Исключили: япшуйкцгщзхфъч

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStateUser("ч", user));
        Assert.assertEquals("Ты проиграл! Загаданое слово: теннис!\n" +
                "Если хочешь сыграть ещё раз напиши /game", gameHandlerTest.updateStateUser("д", user));

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
                            """, gameHandlerTest.updateStateUser("/game", user));
        Assert.assertEquals("""
                Загадано слово: _ _ _ _ _ _
                Тема: спорт
                Количество попыток: 15""", gameHandlerTest.updateStateUser("/easy", user));
        Assert.assertEquals("""
                Правильно!. Осталось попыток: 15
                Исключили:\s

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStateUser("т", user));
        Assert.assertEquals("""
                Не угадал! Осталось попыток: 14
                Исключили: п

                Тема: спорт
                т _ _ _ _ _""", gameHandlerTest.updateStateUser("п", user));
        Assert.assertEquals("""
                Правильно!. Осталось попыток: 14
                Исключили: п

                Тема: спорт
                т _ н н _ _""", gameHandlerTest.updateStateUser("н", user));
        Assert.assertEquals("""
                Правильно!. Осталось попыток: 14
                Исключили: п

                Тема: спорт
                т _ н н _ с""", gameHandlerTest.updateStateUser("с", user));
        Assert.assertEquals("""
                Правильно!. Осталось попыток: 14
                Исключили: п

                Тема: спорт
                т е н н _ с""", gameHandlerTest.updateStateUser("е", user));
        Assert.assertEquals("Молодец! Ты отгадал слово: теннис!\n" +
                "Если хочешь сыграть ещё раз напиши /game", gameHandlerTest.updateStateUser("и", user));

        Assert.assertEquals(totalGameBefore, user.getTotalGameEasy());
        Assert.assertEquals(totalWinGameBefore, user.getTotalWinEasy());
    }
}