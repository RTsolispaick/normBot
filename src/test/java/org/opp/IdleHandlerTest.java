package org.opp;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.opp.core.handler.IdleHandler;
import org.opp.data.models.User;
import org.opp.data.models.Word;
import org.opp.data.models.types.Difficult;
import org.opp.data.models.types.StatusGame;
import org.opp.data.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Тест класса IdleHandlerTest
 */
public class IdleHandlerTest {
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final IdleHandler testIdleHandler = new IdleHandler(userRepository);
    private final User user = new User(0L, "name");

    /**
     * Тест команды /stop
     */
    @Test
    public void stop_Test() {
        Integer totalGameBefore;
        user.setStateGame(new Word("word", "category"));
        user.getUserGame().setDifficult(Difficult.EASY);

        totalGameBefore = user.getTotalGameEasy();
        Assert.assertEquals(testIdleHandler.getResponse("/stop", user), """
                        Игра остановлена!
                        Напиши /game, если хочешь сыграть ещё раз""");
        Assert.assertEquals(user.getTotalGameEasy(), totalGameBefore);

        user.setStateGame(new Word("word", "category"));
        user.getUserGame().setStatusGame(StatusGame.GAME);
        user.getUserGame().setDifficult(Difficult.EASY);

        totalGameBefore = user.getTotalGameEasy() + 1;
        Assert.assertEquals(testIdleHandler.getResponse("/stop", user), """
                        Игра остановлена!
                        Напиши /game, если хочешь сыграть ещё раз""");
        Assert.assertEquals(user.getTotalGameEasy(), totalGameBefore);
    }

    /**
     * Тест команды /stats
     */
    @Test
    public void stats_Test() {
        user.setTotalWinEasy(5);
        user.setTotalWinMedium(3);
        user.setTotalWinHard(1);

        user.setTotalGameEasy(10);
        user.setTotalGameMedium(8);
        user.setTotalGameHard(5);

        String response = testIdleHandler.getResponse("/stats", user);

        String expectedResponse = """
                Количество отгаданных слов:
                Easy = 5
                Medium = 3
                Hard = 1

                Количество неотгаданных слов:
                Easy = 5
                Medium = 5
                Hard = 4

                Количество игр:
                Easy = 10
                Medium = 8
                Hard = 5

                Всего игр: 23""";

        Assert.assertEquals(expectedResponse, response);
    }

    /**
     * Тест команды /top
     */
    @Test
    public void top_Test() {
        List<User> userList = new ArrayList<>();

        User user1 = new User(0L, "vova");
        userList.add(user1);
        User user2 = new User(0L, "peta");
        userList.add(user2);
        User user3 = new User(0L, "goha");
        userList.add(user3);
        User user4 = new User(0L, "ivan");
        userList.add(user4);
        User user5 = new User(0L, "leha");
        userList.add(user5);

        Mockito.when(userRepository.findTop5()).thenReturn(userList);

        Assert.assertEquals("""
                Top:
                1. vova = 0
                2. peta = 0
                3. goha = 0
                4. ivan = 0
                5. leha = 0
                
                Твой рейтинг: 0""", testIdleHandler.getResponse("/top", user1));
    }

    /**
     * Тест на некорректный ввод
     */
    @Test
    public void incorrectInput_Test() {
        Assert.assertEquals("Я тебя не понял. Попробуй написать ещё раз!",
                testIdleHandler.getResponse("incorrectInput", user));
    }
}
