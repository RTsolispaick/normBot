package org.opp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opp.core.Manager;
import org.opp.core.handler.GameHandler;
import org.opp.core.handler.IdleHandler;
import org.opp.data.models.User;

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
        String res = testManager.chooseState("/game", testUser1);
        testUser1.setStateGame(testUser1.getWord());
        Assert.assertEquals(testGameHandler.getAnswer(testUser1.getWord(), testUser1), res);
        testUser1.setStateIdle();
        res = testManager.chooseState("/stop", testUser1);
        Assert.assertEquals( "Вы не находитесь в игре!\nЕсли хотите начать игру напишите /game", res);
        res = testManager.chooseState("Artyom", testUser1);
        Assert.assertEquals(testIdleHandler.getAnswer("Artyom"), res);
        res = testManager.chooseState("/game", testUser2);
        Assert.assertEquals("Игра и так идёт!\nЕсли хотите остановить игру напишите /stop", res);
        res = testManager.chooseState("/stop", testUser2);
        Assert.assertEquals(testIdleHandler.getAnswer("/stop"), res);
        testUser2.setStateGame("sdasda");
        testManager.chooseState("Artyom", testUser2);
        res = testManager.chooseState("Artyom", testUser2);
        Assert.assertEquals(testGameHandler.getAnswer("Artyom", testUser2), res);
    }
}
