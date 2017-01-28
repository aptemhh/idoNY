package org.idony;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.idony.model.Login;
import org.idony.model.Translator;

import java.io.Serializable;
import java.util.List;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // load from different directory
            SessionFactory sessionFactory = new Configuration().configure("/resources/hibernate.cfg.xml")
                    .buildSessionFactory();

            return sessionFactory;

        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static Serializable save(Object o) throws Exception {
        Session session = getSessionFactory().openSession();
        Transaction tx = null;
        Serializable serializable = null;
        try {
            tx = session.beginTransaction();
            serializable = session.save(o);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
        return serializable;
    }

    public static List get(Class aClass) throws Exception {
        Session session = getSessionFactory().openSession();
        try {
            return session.createCriteria(aClass).list();
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
    }

    public static void shutdown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }

    public static Boolean security(String login, String pass) {
        Session session = sessionFactory.openSession();
        Boolean connectors;
        try {
            if (login == null || pass == null) return false;
            connectors =
                    session.createCriteria(Login.class).add(Restrictions.eq("login", login)).
                            add(Restrictions.eq("password", pass))
                            .list().size() != 0;
        } finally {
            session.close();
        }
        return connectors;
    }

    public static void trancate(String table) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.createSQLQuery("truncate table " + table).executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
        } finally {
            session.close();
        }
    }

}