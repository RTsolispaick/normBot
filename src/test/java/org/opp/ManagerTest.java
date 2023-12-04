package org.opp;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.opp.core.Manager;
import org.opp.core.handler.GameSessionHandler;
import org.opp.core.handler.IdleHandler;
import org.opp.data.models.GameSession;
import org.opp.data.models.User;
import org.opp.data.models.types.Platform;
import org.opp.data.models.types.TypeGame;
import org.opp.data.repositories.GameSessionRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Тест класса Manager
 */
public class ManagerTest {
    private final GameSessionHandler testGameSessionHandler = Mockito.mock(GameSessionHandler.class);
    private final GameSessionRepository gameSessionRepository = Mockito.mock(GameSessionRepository.class);
    private final IdleHandler testIdleHandler = Mockito.mock(IdleHandler.class);
    private final Manager testManager = new Manager(testGameSessionHandler, testIdleHandler, gameSessionRepository);
    private final User user = new User(Platform.TG, 24323L, "dfsfs");


    /**
     * Тест для метода chooseState:
     * 1) Для команды /game
     * 2) Для команды /multiplayer
     * 2) Для команды /stop
     * 3) Для произвольного сообщения
     */
    @Test
    public void managerChoose_Test() {
        Mockito.when(testGameSessionHandler.managerGameSession(anyString(), any(GameSession.class), any(User.class)))
                .thenReturn(null);
        Mockito.when(gameSessionRepository.getRandomGameSession())
                .thenReturn(user.getUserGameSession());

        testManager.chooseState("/game", user);
        verify(testGameSessionHandler, times(1))
                .managerGameSession(anyString(), any(GameSession.class), any(User.class));

        testManager.chooseState("/multiplayer", user);
        verify(testGameSessionHandler, times(1))
                .managerGameSession(anyString(), any(GameSession.class), any(User.class));

        user.setStateIdle();
        Assert.assertEquals("Вы не находитесь в игре!\nЕсли хотите начать игру напишите /game или /multiply"
                , testManager.chooseState("/stop", user).get(0).getResponse());

        testManager.chooseState("Artyom", user);
        verify(testIdleHandler, times(1)).getResponse("Artyom", user);

        user.setStateGame(new GameSession(TypeGame.SINGLE));
        testManager.chooseState("/stop", user);
        verify(testGameSessionHandler, times(1))
                .managerGameSession("/stop", user.getUserGameSession(), user);

        user.setStateGame(new GameSession(TypeGame.SINGLE));
        Assert.assertEquals("Игра и так идёт!\nЕсли хотите остановить игру напишите /stop"
                , testManager.chooseState("/game", user).get(0).getResponse());
        Assert.assertEquals("Игра и так идёт!\nЕсли хотите остановить игру напишите /stop"
                , testManager.chooseState("/multiplayer", user).get(0).getResponse());

        testManager.chooseState("Artyom", user);
        verify(testGameSessionHandler,times(1))
                .managerGameSession("Artyom", user.getUserGameSession(), user);
    }
}