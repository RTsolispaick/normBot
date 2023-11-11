package org.opp.data.dao;

import org.hibernate.Session;
import org.opp.data.models.Word;
import org.opp.data.utils.HibernateSessionFactoryUtil;

public class WordDAO {
    public Word getRandomWord() {
        Word word = null;

        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            word = session.createQuery(
                            "select u from Word u ORDER BY RANDOM()"
                            , Word.class)
                    .setMaxResults(1)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return word;

    }
}
