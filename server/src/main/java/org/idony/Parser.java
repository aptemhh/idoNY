package org.idony;

import org.hibernate.Session;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by idony on 23.10.16.
 */
public class Parser implements Runnable {
    Socket socket;
    DataInputStream in;
    DataOutputStream out;

    public Parser setSocket(Socket socket) throws IOException {
        this.socket = socket;
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        return this;
    }

    public void run() {
        try {
            String string=in.readUTF();

//            Session session = HibernateUtil.getSessionFactory().openSession();
//            session.flush();
//            session.beginTransaction().begin();
//
//            session.getTransaction().commit();
//            session.close();
            out.writeUTF("utfStr");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
