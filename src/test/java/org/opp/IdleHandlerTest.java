package org.opp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opp.core.handler.IdleHandler;
import org.opp.data.models.User;

/**
 * Тест класса IdleHandlerTest
 */
public class IdleHandlerTest {
    private IdleHandler testIdleHandler;
    private User user;

    /**
     *  Инициализируем объект тестируемого класса
     */
    @Before
    public void setUp() {
        testIdleHandler = new IdleHandler();
        user = new User(123123L, "asdas");
    }

    /**
     * Тест для метода messageHandler:
     * 1) Для произвольного сообщения
     * 2) Для команды /stats
     */
    @Test
    public void messageHandler_Test() {
        Assert.assertEquals("Я тебя не понял. Попробуй написать ещё раз!", testIdleHandler.getAnswer("Kostya", user));
        Assert.assertEquals("Количество побед: " + user.getTotalWin() + "\n" +
                "Количество игр: " + user.getTotalGame(), testIdleHandler.getAnswer("/stats", user));
    }
}
