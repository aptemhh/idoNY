package org.idony;

import org.hibernate.SessionFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by idony on 22.10.16.
 */
public class Main implements Runnable {
    static {
        Logger.getLogger("org.hibernate").setLevel(Level.WARNING);
    }

    public static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public static void main(final String[] args) throws Exception {
        System.out.println("Start");

//        Session session = sessionFactory.openSession();
//        List<Login> logins = session.getNamedQuery("getLogins").list();
//        session.close();

        System.out.println("Finish");

        sessionFactory.close();
    }

    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(2000);
            for (;;) {
                Socket socket = serverSocket.accept();
                new Thread(new Parser().setSocket(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
