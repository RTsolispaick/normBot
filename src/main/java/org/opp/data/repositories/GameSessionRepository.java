package org.opp.data.repositories;

import org.opp.data.dao.GameSessionDAO;
import org.opp.data.models.GameSession;


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
        return gameSessionDAO.getRandomGameSession();
    }

}