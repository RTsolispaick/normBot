package org.opp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opp.core.handler.IdleHandler;

/**
 * Тест класса IdleHandlerTest
 */
public class IdleHandlerTest {
    private IdleHandler testIdleHandler;

    /**
     *  Инициализируем объект тестируемого класса
     */
    @Before
    public void setUp() {
        testIdleHandler = new IdleHandler();
    }

    /**
     * Тест для метода messageHandler:
     * 1) Для произвольного сообщения
     */
    @Test
    public void messageHandler_Test() {
        Assert.assertEquals("Я тебя не понял. Попробуй написать ещё раз!", testIdleHandler.getAnswer("Kostya"));
    }
}
