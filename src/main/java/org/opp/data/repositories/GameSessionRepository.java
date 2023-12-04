package org.opp.data.repositories;

import org.opp.data.dao.GameSessionDAO;
import org.opp.data.models.GameSession;
import org.opp.data.models.types.TypeGame;

public class GameSessionRepository {
    private final GameSessionDAO gameSessionDAO;

    public GameSessionRepository() {
        this.gameSessionDAO = new GameSessionDAO();
    }

    public void delete(GameSession gameSession) {
        gameSessionDAO.delete(gameSession);
    }

    /**
     * Возвращает пользователю случайную игровую сессию или создаёт новую
     */
    public GameSession getRandomGameSession() {
        GameSession gameSession = gameSessionDAO.getRandomGameSession();

        if (gameSession == null) {
            gameSession = new GameSession(TypeGame.MULTI);
            gameSessionDAO.save(gameSession);
        }

        return gameSession;
    }

}