package org.opp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.opp.core.Manager;
import org.opp.core.handler.GameHandler;
import org.opp.core.handler.IdleHandler;
import org.opp.data.models.User;
import org.opp.data.models.Word;
import org.opp.data.repositories.WordRepository;

/**
 * Тест класса Manager
 */
public class ManagerTest {
    private Manager testManager;
    private User testUser1, testUser2;
    private GameHandler testGameHandler;
    private IdleHandler testIdleHandler;
    private WordRepository wordRepository;

    /**
     *  Инициализируем объект тестируемого класса
     */
    @Before
    public void setUp() {
        testIdleHandler = new IdleHandler();
        testUser1 = new User(24323L, "dfsfs");
        testUser2 = new User(43423L, "dfsfs");
        testUser2.setStateGame(new Word("теннис", "спорт"));
    }

    /**
     * Тест для метода chooseState:
     * 1) Для команды /game
     * 2) Для команды /stop
     * 3) Для произвольного сообщения
     */
    @Test
    public void managerChoose_Test() {
        WordRepository wordRepository = Mockito.mock(WordRepository.class);
        GameHandler testGameHandler = Mockito.mock(GameHandler.class);

        Mockito.when(wordRepository.getRandomWord()).thenReturn(new Word("теннис", "спорт"));

        testGameHandler.setWordRepository(wordRepository);

        Mockito.when(testGameHandler.getAnswer("/game", testUser1)).thenReturn("uu");
        Mockito.when(testGameHandler.getAnswer("/game", testUser2)).thenReturn("uu");
        Mockito.when(testGameHandler.getAnswer("Artyom", testUser2)).thenReturn("uu");

        testManager = new Manager(testGameHandler);

        String res = testManager.chooseState("/game", testUser1);
        Assert.assertEquals(testGameHandler.getAnswer("/game", testUser1), res);
        testUser1.setStateIdle();
        res = testManager.chooseState("/stop", testUser1);
        Assert.assertEquals( "Вы не находитесь в игре!\nЕсли хотите начать игру напишите /game", res);
        res = testManager.chooseState("Artyom", testUser1);
        Assert.assertEquals(testIdleHandler.getAnswer("Artyom", testUser1), res);
        res = testManager.chooseState("/stop", testUser2);
        Assert.assertEquals(testIdleHandler.getAnswer("/stop", testUser2), res);
        testUser2.setStateGame(new Word("теннис", "спорт"));
        res = testManager.chooseState("/game", testUser2);
        Assert.assertEquals("Игра и так идёт!\nЕсли хотите остановить игру напишите /stop", res);
        res = testManager.chooseState("Artyom", testUser2);
        Assert.assertEquals(testGameHandler.getAnswer("Artyom", testUser2), res);
    }
}
