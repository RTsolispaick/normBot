package org.opp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opp.core.Logic;

/**
 * Тест класса Logic
 */
public class LogicTest {
    private Logic testLogic;

    /**
     *  Инициализируем объект тестируемого класса
     */
    @BeforeEach
    public void setUp() {
        testLogic = new Logic();
    }

    /**
     * Тест для метода messageHandler:
     * 1) Для команды /start
     * 2) Для команды /help
     * 3) Для произвольного сообщения
     */
    @Test
    public void messageHandler_Test() {
        Assertions.assertEquals(testLogic.massageHandler("/start"), "Hello, write something!");
        Assertions.assertEquals(testLogic.massageHandler("/help"), "Just write something.");
        Assertions.assertEquals(testLogic.massageHandler("Kostya"), "Kostya");
    }
}
