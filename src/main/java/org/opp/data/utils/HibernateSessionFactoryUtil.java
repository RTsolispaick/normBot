package org.opp.data.utils;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.opp.data.models.Game;
import org.opp.data.models.GameSession;
import org.opp.data.models.User;
import org.opp.data.models.Word;
import org.opp.utils.Config;


public class HibernateSessionFactoryUtil {
    private static SessionFactory sessionFactory;

    private HibernateSessionFactoryUtil() {}

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Config cfg = new Config();
                Configuration configuration = new Configuration().configure();
                configuration.setProperty("hibernate.connection.url", "jdbc:postgresql://" + cfg.getDBhost() + ":" + cfg.getDBport() + "/" + cfg.getDBname());
                configuration.setProperty("hibernate.connection.username", cfg.getDBuser());
                configuration.setProperty("hibernate.connection.password", cfg.getDBpass());
                configuration.addAnnotatedClass(User.class);
                configuration.addAnnotatedClass(Word.class);
                configuration.addAnnotatedClass(Game.class);
                configuration.addAnnotatedClass(GameSession.class);
                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());
            } catch (Exception e) {
                System.out.println("Exception:" + e);
            }
        }
        return sessionFactory;
    }


}