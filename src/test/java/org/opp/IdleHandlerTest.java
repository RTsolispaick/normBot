package org.opp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opp.core.handler.IdleHandler;

/**
 * Тест класса Logic
 */
public class IdleHandlerTest {
    private IdleHandler testIdleHandler;

    /**
     *  Инициализируем объект тестируемого класса
     */
    @BeforeEach
    public void setUp() {
        testIdleHandler = new IdleHandler();
    }

    /**
     * Тест для метода messageHandler:
     * 1) Для команды /start
     * 2) Для команды /help
     * 3) Для произвольного сообщения
     */
    @Test
    public void messageHandler_Test() {
        Assertions.assertEquals("Я тебя не понял. Попробуй написать ещё раз!", testIdleHandler.getAnswer("Kostya"));
    }
}
