package org.opp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opp.core.Logic;

public class LogicTest {
    @Test
    public void messageHandler_Test() {
        Assertions.assertEquals(Logic.massageHandler("/start"), "Hello, write something!");
        Assertions.assertEquals(Logic.massageHandler("/help"), "Just write something.");
        Assertions.assertEquals(Logic.massageHandler("Kostya"), "Kostya");
    }
}
