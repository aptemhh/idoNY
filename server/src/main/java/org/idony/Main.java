package org.idony;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.idony.model.Login;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by idony on 22.10.16.
 */
public class Main {
    static
    {
        Logger.getLogger("org.hibernate").setLevel(Level.WARNING);
    }
    public static SessionFactory sessionFactory=HibernateUtil.getSessionFactory();
    public static void main(final String[] args) throws Exception {
        System.out.println("Start");

        Session session =  sessionFactory.openSession();
        List<Login> logins=session.getNamedQuery("getLogin").list();
        session.close();

        System.out.println("Finish");

        sessionFactory.close();
    }
}
