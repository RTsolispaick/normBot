package org.opp.data.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.opp.data.models.User;
import org.opp.data.utils.HibernateSessionFactoryUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Доступ к данным пользователя через таблицу данных
 */
public class UserDAO {
    /**
     * Поиск по id (в платформе) пользователя
     *
     * @param chat_id - id на платформе
     * @return Объект user c данным id
     */
    public User findByChatId(Long chat_id) {
        User user = null;
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        try {
            user = session.createQuery(
                            "select u from User u where u.chat_id = :chat_id",
                            User.class)
                    .setParameter("chat_id", chat_id)
                    .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return user;
    }

    public List<User> findTop5() {
        List<User> users = new ArrayList<>();
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        try {
            users = session.createQuery(
                            "from User u order by u.ratingUser desc",
                            User.class)
                    .setMaxResults(5)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return users;
    }

    public void save(User user) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        try {
            session.save(user);
            tx1.commit();
        } catch (final Exception e) {
            tx1.rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    public void update(User user) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        try {
            session.update(user);
            tx1.commit();
        } catch (final Exception e) {
            tx1.rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }


}
