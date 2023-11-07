package org.opp;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.opp.core.Manager;
import org.opp.core.handler.IdleHandler;
import org.opp.data.models.User;
import org.opp.data.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Тест класса IdleHandlerTest
 */
public class IdleHandlerTest {
    private IdleHandler testIdleHandler;
    private UserRepository userRepository;




    /**
     * Тест для класса IdleHandler на некорректный ввод
     */
    @Test
    public void idleHandlerUncorectInput_Test() {
        User user = new User();
        testIdleHandler = new IdleHandler();

        Assert.assertEquals("Я тебя не понял. Попробуй написать ещё раз!", testIdleHandler.getAnswer("Kostya", user));
    }

    /**
     * Тест для команды /top
     */
    @Test
    public void top_Test() {
        userRepository = Mockito.mock(UserRepository.class);
        List<User> userList = new ArrayList<>();

        User user1 = new User(0L, "vova");
        user1.setStateIdle();
        userList.add(user1);
        User user2 = new User(0L, "peta");
        user2.setStateIdle();
        userList.add(user2);

        Mockito.when(userRepository.findTop5()).thenReturn(userList);
        testIdleHandler = new IdleHandler(userRepository);

        Assert.assertEquals("""
                Top:
                1. vova = 0
                2. peta = 0

                Твой рейтинг: 0""", testIdleHandler.getAnswer("/top", user1));
    }

    /**
     * Тест для команды /stats
     */
    @Test
    public void stats_Test() {
        userRepository = Mockito.mock(UserRepository.class);
        testIdleHandler = new IdleHandler();

        User user = new User(0L, "asda");

        Mockito.when(userRepository.findByChatID(0L)).thenReturn(user);
        Mockito.doNothing().when(userRepository).update(user);
        Mockito.doNothing().when(userRepository).save(user);

        Manager testManager = new Manager(userRepository);

        Assert.assertEquals("Количество побед: 0\n" +
                "Количество игр: 0", testIdleHandler.getAnswer("/stats", user));


        testManager.chooseState("/game", 0L, "asda");

        Assert.assertEquals("Количество побед: 0\n" +
                "Количество игр: 1", testIdleHandler.getAnswer("/stats", user));

        user.setWord("о");
        user.setGameViewOfTheWord("яяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяяя");
        testManager.chooseState("о", 0L, "asda");

        Assert.assertEquals("Количество побед: 1\n" +
                "Количество игр: 1", testIdleHandler.getAnswer("/stats", user));
    }
}
