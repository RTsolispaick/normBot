package org.opp.data.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.opp.data.models.GameSession;
import org.opp.data.models.types.StateSession;
import org.opp.data.models.types.TypeGame;
import org.opp.data.utils.HibernateSessionFactoryUtil;

/**
 * Доступ к данным из таблицы сессий
 */
public class GameSessionDAO {

    public void delete(GameSession gameSession) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();

        Transaction tx1 = session.beginTransaction();
        try {
            gameSession.getLinkedUsers().forEach(user -> {
                user.setStateIdle();
                session.update(user);
            });
            session.delete(gameSession);
            tx1.commit();
        } catch (final Exception e) {
            tx1.rollback();
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    /**
     * Метод ищет доступную сессию для многопользовательской сессии
     */
    public GameSession getRandomGameSession() {
        GameSession gameSession = null;
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        try {
            gameSession = session.createQuery(
                            "SELECT u FROM GameSession u WHERE u.typeGame = :desiredValue AND u.stateSession = :stateSession ORDER BY RANDOM()"
                            , GameSession.class)
                    .setParameter("desiredValue", TypeGame.MULTI)
                    .setParameter("stateSession", StateSession.WAITING_FOR_PLAYERS)
                    .setMaxResults(1)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            session.close();
        }

        return gameSession;
    }
}