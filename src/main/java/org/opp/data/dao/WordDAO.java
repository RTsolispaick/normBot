package org.opp.data.dao;

import org.hibernate.Session;
import org.opp.data.models.Word;
import org.opp.data.utils.HibernateSessionFactoryUtil;
/**
 * Доступ к словам через базу данных слов
 * */
public class WordDAO {
    /**
     * Выбор случайного слова из база данных для игры
     * @return слово для игры в висилицу
     */
    public Word getRandomWord() {
        Word word = null;
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        try {
            word = session.createQuery(
                            "select u from Word u ORDER BY RANDOM()"
                            , Word.class)
                    .setMaxResults(1)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            session.close();
        }

        return word;

    }
}
