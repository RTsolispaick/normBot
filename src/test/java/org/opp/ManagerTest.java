package org.opp;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.opp.core.Manager;
import org.opp.core.handler.GameHandler;
import org.opp.core.handler.IdleHandler;
import org.opp.data.models.User;
import org.opp.data.models.Word;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Тест класса Manager
 */
public class ManagerTest {
    private final GameHandler testGameHandler = Mockito.mock(GameHandler.class);
    private final IdleHandler testIdleHandler = Mockito.mock(IdleHandler.class);
    private final Manager testManager = new Manager(testGameHandler, testIdleHandler);
    private final User user = new User(24323L, "dfsfs");


    /**
     * Тест для метода chooseState:
     * 1) Для команды /game
     * 2) Для команды /stop
     * 3) Для произвольного сообщения
     */
    @Test
    public void managerChoose_Test() {
        testManager.chooseState("/game", user);
        verify(testGameHandler, times(1)).updateStateUser(anyString(), any(User.class));

        user.setStateIdle();
        Assert.assertEquals( "Вы не находитесь в игре!\nЕсли хотите начать игру напишите /game"
                , testManager.chooseState("/stop", user));

        testManager.chooseState("Artyom", user);
        verify(testIdleHandler, times(1)).getResponse("Artyom", user);

        user.setStateGame(new Word("теннис", "спорт"));
        testManager.chooseState("/stop", user);
        verify(testIdleHandler, times(1)).getResponse("/stop", user);

        user.setStateGame(new Word("теннис", "спорт"));
        Assert.assertEquals("Игра и так идёт!\nЕсли хотите остановить игру напишите /stop"
                , testManager.chooseState("/game", user));

        testManager.chooseState("Artyom", user);
        verify(testGameHandler,times(1)).updateStateUser("Artyom", user);
    }
}
