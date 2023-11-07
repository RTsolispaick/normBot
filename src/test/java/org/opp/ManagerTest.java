package org.opp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.opp.core.Manager;
import org.opp.core.handler.GameHandler;
import org.opp.core.handler.IdleHandler;
import org.opp.data.models.User;
import org.opp.data.repositories.UserRepository;

/**
 * Тест класса Manager
 */
public class ManagerTest {
    private Manager testManager;
    private User testUser1, testUser2;
    private GameHandler testGameHandler;
    private IdleHandler testIdleHandler;

    /**
     *  Инициализируем объект тестируемого класса
     */
    @Before
    public void setUp() {
        testGameHandler = new GameHandler();
        testIdleHandler = new IdleHandler();
        testManager = new Manager();
        testUser1 = new User(24323L, "dfsfs");
        testUser2 = new User(43423L, "dfsfs");
        testUser2.setStateGame("sdadsa");
    }

    /**
     * Тест для метода chooseState:
     * 1) Для команды /game
     * 2) Для команды /stop
     * 3) Для произвольного сообщения
     */
    @Test
    public void managerChoose_Test() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        testIdleHandler = new IdleHandler();

        Mockito.when(userRepository.findByChatID(24323L)).thenReturn(testUser1);
        Mockito.doNothing().when(userRepository).update(testUser1);
        Mockito.doNothing().when(userRepository).save(testUser1);
        Mockito.when(userRepository.findByChatID(43423L)).thenReturn(testUser2);
        Mockito.doNothing().when(userRepository).update(testUser2);
        Mockito.doNothing().when(userRepository).save(testUser2);

        testManager = new Manager(userRepository);

        String res = testManager.chooseState("/game", 24323L, "dfsfs");
        testUser1.setStateGame(testUser1.getWord());
        Assert.assertEquals(testGameHandler.getAnswer(testUser1.getWord(), testUser1), res);
        testUser1.setStateIdle();
        res = testManager.chooseState("/stop", 24323L, "dfsfs");
        Assert.assertEquals( "Вы не находитесь в игре!\nЕсли хотите начать игру напишите /game", res);
        res = testManager.chooseState("Artyom", 24323L, "dfsfs");
        Assert.assertEquals(testIdleHandler.getAnswer("Artyom", testUser1), res);
        res = testManager.chooseState("/game", 43423L, "dfsfs");
        Assert.assertEquals("Игра и так идёт!\nЕсли хотите остановить игру напишите /stop", res);
        res = testManager.chooseState("/stop", 43423L, "dfsfs");
        Assert.assertEquals(testIdleHandler.getAnswer("/stop", testUser2), res);
        testUser2.setStateGame("sdasda");
        testManager.chooseState("Artyom", 43423L, "dfsfs");
        res = testManager.chooseState("Artyom", 43423L, "dfsfs");
        Assert.assertEquals(testGameHandler.getAnswer("Artyom", testUser2), res);
    }
}
