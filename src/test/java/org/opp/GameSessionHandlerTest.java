package org.opp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.opp.core.handler.GameSessionHandler;
import org.opp.data.models.GameSession;
import org.opp.data.models.Response;
import org.opp.data.models.User;
import org.opp.data.models.Word;
import org.opp.data.models.types.Platform;
import org.opp.data.models.types.StateSession;
import org.opp.data.models.types.TypeGame;
import org.opp.data.repositories.GameSessionRepository;
import org.opp.data.repositories.WordRepository;

import java.util.List;

import static org.mockito.Mockito.*;

public class GameSessionHandlerTest {
    private final GameSessionRepository gameSessionRepositoryTest = Mockito.mock(GameSessionRepository.class);
    private final WordRepository wordRepositoryTest = Mockito.mock(WordRepository.class);
    private final GameSessionHandler gameSessionHandlerTest = new GameSessionHandler(gameSessionRepositoryTest,
            wordRepositoryTest);
    private User userOne;
    private User userTwo;
    private GameSession gameSessionSingle;
    private GameSession gameSessionMulti;

    @Before
    public void SetUp() {
        userOne = new User(Platform.VK, 0L, "peta");
        userOne.setId(0);
        userTwo = new User(Platform.TG, 1L, "vova");
        userTwo.setId(1);
        gameSessionSingle = new GameSession(TypeGame.SINGLE);
        gameSessionMulti = new GameSession(TypeGame.MULTI);
    }

    /**
     * Проверка остановки игры в случае {@link TypeGame#SINGLE}
     */
    @Test
    public void managerGameSessionTestStopSingle() {
        Mockito.doNothing().when(gameSessionRepositoryTest).delete(gameSessionSingle);

        userOne.setStateGame(gameSessionSingle);
        Assert.assertEquals("Игра остановлена!",
                gameSessionHandlerTest
                        .managerGameSession("/stop", gameSessionSingle, userOne).get(0).getResponse());
        verify(gameSessionRepositoryTest, times(1)).delete(gameSessionSingle);
    }

    /**
     * Проверка команды "/stop" в случае одного пользователя для {@link TypeGame#MULTI}
     */
    @Test
    public void managerGameSessionTestStopMultiOnePlayer() {
        Mockito.doNothing().when(gameSessionRepositoryTest).delete(gameSessionMulti);

        userOne.setStateGame(gameSessionMulti);
        gameSessionMulti.setStateSession(StateSession.WAITING_FOR_PLAYERS);

        List<Response> list =  gameSessionHandlerTest.managerGameSession("/stop", gameSessionMulti, userOne);

        Assert.assertEquals("Игра остановлена!",
                list.get(0).getResponse());
        Assert.assertEquals(userOne.getChat_id(),
                list.get(0).getID());
        Assert.assertEquals(Integer.valueOf(1),
                Integer.valueOf(list.size()));
        verify(gameSessionRepositoryTest, times(1)).delete(gameSessionMulti);
    }

    /**
     * Проверка команды "/stop" в случае двух пользователей для {@link TypeGame#MULTI}
     */
    @Test
    public void managerGameSessionTestStopMultiTwoPlayer() {
        Mockito.doNothing().when(gameSessionRepositoryTest).delete(gameSessionMulti);

        userOne.setStateGame(gameSessionMulti);
        userTwo.setStateGame(gameSessionMulti);

        List<Response> list =  gameSessionHandlerTest.managerGameSession("/stop", gameSessionMulti, userOne);

        Assert.assertEquals("Игра остановлена!",
                list.get(0).getResponse());
        Assert.assertEquals(Long.valueOf(0L), list.get(0).getID());
        Assert.assertEquals("Игра остновлена вашим опонентом!",
                list.get(1).getResponse());
        Assert.assertEquals(Long.valueOf(1L), list.get(1).getID());
        verify(gameSessionRepositoryTest, times(1)).delete(gameSessionMulti);
    }

    /**
     * Тест метода handleSingleSession
     */
    @Test
    public void managerGameSessionTestHandleSingleSession() {
        Mockito.when(wordRepositoryTest.getRandomWord()).thenReturn(new Word("о", "о"));
        Mockito.doNothing().when(gameSessionRepositoryTest).delete(gameSessionMulti);

        List<Response> responseList = gameSessionHandlerTest
                .managerGameSession("/game", gameSessionSingle, userOne);

        verify(wordRepositoryTest, times(1)).getRandomWord();
        Assert.assertEquals("""
                            Выбери сложность:
                            /easy - 15 попыток
                            /medium - 10 попыток
                            /hard - 5 попыток
                            """, responseList.get(0).getResponse());
        Assert.assertEquals(Long.valueOf(0L), responseList.get(0).getID());
        verify(gameSessionRepositoryTest, never()).delete(gameSessionSingle);

        responseList = gameSessionHandlerTest
                .managerGameSession("/easy", gameSessionSingle, userOne);

        verify(wordRepositoryTest, times(1)).getRandomWord();
        Assert.assertEquals("""
                Загадано слово: _
                Тема: о
                Количество попыток: 15""", responseList.get(0).getResponse());
        Assert.assertEquals(Long.valueOf(0L), responseList.get(0).getID());
        verify(gameSessionRepositoryTest, never()).delete(gameSessionSingle);

        responseList = gameSessionHandlerTest
                .managerGameSession("о", gameSessionSingle, userOne);

        verify(wordRepositoryTest, times(1)).getRandomWord();
        Assert.assertEquals("Молодец! Ты отгадал слово: о!", responseList.get(0).getResponse());
        Assert.assertEquals(Long.valueOf(0L), responseList.get(0).getID());
        verify(gameSessionRepositoryTest, times(1)).delete(gameSessionSingle);
    }

    /**
     * Тест метода handleInitState
     */
    @Test
    public void managerGameSessionTestHandleInitState() {
        List<Response> responseList = gameSessionHandlerTest
                .managerGameSession("/multiplayer", gameSessionMulti, userOne);

        Assert.assertEquals(StateSession.WAITING_FOR_PLAYERS,
                gameSessionMulti.getStateSession());
        Assert.assertEquals("Игра создана! Ищем вам соперника...",
                responseList.get(0).getResponse());
        Assert.assertEquals(Long.valueOf(0L), responseList.get(0).getID());
    }

    /**
     * Тест метода handleWaitingForPlayersState
     */
    @Test
    public void managerGameSessionTestHandleWaitingForPlayersState() {
        gameSessionHandlerTest
                .managerGameSession("/multiplayer", gameSessionMulti, userOne);

        List<Response> responseList = gameSessionHandlerTest
                .managerGameSession("/multiplayer", gameSessionMulti, userOne);

        Assert.assertEquals(StateSession.WAITING_FOR_PLAYERS,
                gameSessionMulti.getStateSession());
        Assert.assertEquals("Подожди второго игрока!\nЕсли ты не хочешь ждать напиши /stop",
                responseList.get(0).getResponse());
        Assert.assertEquals(Long.valueOf(0L), responseList.get(0).getID());

        responseList = gameSessionHandlerTest
                .managerGameSession("/multiplayer", gameSessionMulti, userTwo);

        Assert.assertEquals(StateSession.MAKE_WORD,
                gameSessionMulti.getStateSession());

        Assert.assertEquals("Игра найдена!\nВаш соперник: peta",
                responseList.get(0).getResponse());
        Assert.assertEquals(Long.valueOf(1L), responseList.get(0).getID());

        Assert.assertEquals("Ваш соперник найден!\nВаш соперник: vova",
                responseList.get(1).getResponse());
        Assert.assertEquals(Long.valueOf(0L), responseList.get(1).getID());

        Assert.assertEquals("Загадайте слово!",
                responseList.get(2).getResponse());
        Assert.assertEquals(Long.valueOf(1L), responseList.get(2).getID());

        Assert.assertEquals("Cлово загадывает ваш оппонент!",
                responseList.get(3).getResponse());
        Assert.assertEquals(Long.valueOf(0L), responseList.get(3).getID());
    }

    /**
     * Тест метода handleMakeWordState
     */
    @Test
    public void managerGameSessionTestHandleMakeWordState() {
        gameSessionHandlerTest
                .managerGameSession("/multiplayer", gameSessionMulti, userOne);
        gameSessionHandlerTest
                .managerGameSession("/multiplayer", gameSessionMulti, userTwo);

        List<Response> responseList = gameSessionHandlerTest
                .managerGameSession("о", gameSessionMulti, userOne);

        Assert.assertEquals("Ваш опонент загадывает вам слово!\nДождитесь его ответа",
                responseList.get(0).getResponse());
        Assert.assertEquals(Long.valueOf(0L), responseList.get(0).getID());

        responseList = gameSessionHandlerTest
                .managerGameSession("tennis", gameSessionMulti, userTwo);

        Assert.assertEquals("Слово должно быть на русском языке и не содержать пробелов!",
                responseList.get(0).getResponse());
        Assert.assertEquals(Long.valueOf(1L), responseList.get(0).getID());

        responseList = gameSessionHandlerTest
                .managerGameSession("о", gameSessionMulti, userTwo);

        Assert.assertEquals( "о"
                ,gameSessionMulti.getGame().getWord());
        Assert.assertEquals("Теперь напишите категорию к которой относится это слово!",
                responseList.get(0).getResponse());
        Assert.assertEquals(Long.valueOf(1L), responseList.get(0).getID());

        responseList = gameSessionHandlerTest
                .managerGameSession("а", gameSessionMulti, userTwo);

        Assert.assertEquals("а",
                gameSessionMulti.getGame().getCategory());
        Assert.assertEquals(StateSession.GAME,
                gameSessionMulti.getStateSession());

        Assert.assertEquals("Слово загадано!",
                responseList.get(0).getResponse());
        Assert.assertEquals(Long.valueOf(1L), responseList.get(0).getID());
        Assert.assertEquals("Вам загадали слово!",
                responseList.get(1).getResponse());
        Assert.assertEquals(Long.valueOf(0L), responseList.get(1).getID());
        Assert.assertEquals("""
                            Выбери сложность:
                            /easy - 15 попыток
                            /medium - 10 попыток
                            /hard - 5 попыток
                            """,
                responseList.get(2).getResponse());
        Assert.assertEquals(Long.valueOf(0L), responseList.get(2).getID());
    }

    /**
     * Тест метода handleGameState
     */
    @Test
    public void managerGameSessionTestHandleGameState() {
        Mockito.doNothing().when(gameSessionRepositoryTest).delete(gameSessionMulti);

        gameSessionHandlerTest
                .managerGameSession("/multiplayer", gameSessionMulti, userOne);
        gameSessionHandlerTest
                .managerGameSession("/multiplayer", gameSessionMulti, userTwo);
        gameSessionHandlerTest
                .managerGameSession("о", gameSessionMulti, userTwo);
        gameSessionHandlerTest
                .managerGameSession("а", gameSessionMulti, userTwo);

        List<Response> responseList = gameSessionHandlerTest
                .managerGameSession("tennis", gameSessionMulti, userTwo);

        Assert.assertEquals("Cейчас играет другой игрок!",
                responseList.get(0).getResponse());
        Assert.assertEquals(Long.valueOf(1L), responseList.get(0).getID());

        responseList = gameSessionHandlerTest
                .managerGameSession("/easy", gameSessionMulti, userOne);

        Assert.assertEquals("""
                               Загадано слово: _
                               Тема: а
                               Количество попыток: 15""",
                responseList.get(0).getResponse());
        Assert.assertEquals(Long.valueOf(0L), responseList.get(0).getID());
        Assert.assertEquals("peta: /easy",
                responseList.get(1).getResponse());
        Assert.assertEquals(Long.valueOf(1L), responseList.get(1).getID());
        Assert.assertEquals("""
                               Загадано слово: _
                               Тема: а
                               Количество попыток: 15""",
                responseList.get(2).getResponse());
        Assert.assertEquals(Long.valueOf(1L), responseList.get(2).getID());

        responseList = gameSessionHandlerTest
                .managerGameSession("о", gameSessionMulti, userOne);

        Assert.assertEquals("Молодец! Ты отгадал слово: о!",
                responseList.get(0).getResponse());
        Assert.assertEquals(Long.valueOf(0L), responseList.get(0).getID());
        Assert.assertEquals("peta: о",
                responseList.get(1).getResponse());
        Assert.assertEquals(Long.valueOf(1L), responseList.get(1).getID());
        Assert.assertEquals("Молодец! Ты отгадал слово: о!",
                responseList.get(2).getResponse());
        Assert.assertEquals(Long.valueOf(1L), responseList.get(2).getID());

        Assert.assertNull(gameSessionMulti.getGame().getWord());
        Assert.assertNull(gameSessionMulti.getGame().getCategory());
        Assert.assertEquals(StateSession.MAKE_WORD,
                gameSessionMulti.getStateSession());

        Assert.assertEquals("Теперь слово загадывают вам!",
                responseList.get(3).getResponse());
        Assert.assertEquals(Long.valueOf(1L), responseList.get(3).getID());
        Assert.assertEquals("Теперь ваша очередь загадывать слово!",
                responseList.get(4).getResponse());
        Assert.assertEquals(Long.valueOf(0L), responseList.get(4).getID());

        gameSessionHandlerTest
                .managerGameSession("о", gameSessionMulti, userOne);
        gameSessionHandlerTest
                .managerGameSession("а", gameSessionMulti, userOne);

        responseList = gameSessionHandlerTest
                .managerGameSession("/easy", gameSessionMulti, userTwo);

        Assert.assertEquals("""
                               Загадано слово: _
                               Тема: а
                               Количество попыток: 15""",
                responseList.get(0).getResponse());
        Assert.assertEquals(Long.valueOf(1L), responseList.get(0).getID());
        Assert.assertEquals("vova: /easy",
                responseList.get(1).getResponse());
        Assert.assertEquals(Long.valueOf(0L), responseList.get(1).getID());
        Assert.assertEquals("""
                               Загадано слово: _
                               Тема: а
                               Количество попыток: 15""",
                responseList.get(2).getResponse());
        Assert.assertEquals(Long.valueOf(0L), responseList.get(2).getID());

        responseList = gameSessionHandlerTest
                .managerGameSession("о", gameSessionMulti, userTwo);

        Assert.assertEquals("Молодец! Ты отгадал слово: о!",
                responseList.get(0).getResponse());
        Assert.assertEquals(Long.valueOf(1L), responseList.get(0).getID());
        Assert.assertEquals("vova: о",
                responseList.get(1).getResponse());
        Assert.assertEquals(Long.valueOf(0L), responseList.get(1).getID());
        Assert.assertEquals("Молодец! Ты отгадал слово: о!",
                responseList.get(2).getResponse());
        Assert.assertEquals(Long.valueOf(0L), responseList.get(2).getID());

        verify(gameSessionRepositoryTest, times(1)).delete(gameSessionMulti);

        Assert.assertEquals("Игра закончена!",
                responseList.get(3).getResponse());
        Assert.assertEquals(Long.valueOf(1L), responseList.get(3).getID());
        Assert.assertEquals("Игра закончена!",
                responseList.get(4).getResponse());
        Assert.assertEquals(Long.valueOf(0L), responseList.get(4).getID());
    }
}