package org.opp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opp.core.Manager;
import org.opp.core.handler.GameHandler;
import org.opp.core.handler.IdleHandler;
import org.opp.essence.User;

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
    @BeforeEach
    public void setUp() {
        testGameHandler = new GameHandler();
        testManager = new Manager();
        testUser1 = new User();
        testUser2 = new User();
        testUser2.setStateGame();
        testIdleHandler = new IdleHandler();
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
        Assertions.assertEquals(testGameHandler.startGame(testUser1.getWord(), testUser1), res);
        testUser1.setStateIdle();
        res = testManager.chooseState("/stop", testUser1);
        Assertions.assertEquals( "Вы не находитесь в игре!\nЕсли хотите начать игру напишите /game", res);
        res = testManager.chooseState("Artyom", testUser1);
        Assertions.assertEquals(testIdleHandler.getAnswer("Artyom"), res);
        res = testManager.chooseState("/game", testUser2);
        Assertions.assertEquals("Игра и так идёт!\nЕсли хотите остановить игру напишите /stop", res);
        res = testManager.chooseState("/stop", testUser2);
        Assertions.assertEquals(testIdleHandler.getAnswer("/stop"), res);
        testUser2.setStateGame();
        res = testManager.chooseState("Artyom", testUser2);
        Assertions.assertEquals(testGameHandler.getAnswer("Artyom", testUser2), res);
    }
}
