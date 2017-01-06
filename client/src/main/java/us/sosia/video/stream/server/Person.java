package us.sosia.video.stream.server;

import java.net.NetworkInterface;
import java.net.SocketException;

/**
 * Created by idony on 06.01.17.
 */
public class Person {
    String login;
    String password;
    static Person person=new Person();
    public static Integer portT= 20000;
    public static Integer portS= 20001;
    public static Integer portP= 15044;
    public static String ip;
    public static String keyS="123";
    public static String keyP="123";
    {
        try {
            ip = NetworkInterface.getNetworkInterfaces().nextElement().getInetAddresses().nextElement().getCanonicalHostName();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
    public String getLogin() {
        return person.login;
    }

    public void setLogin(String login) {
        person.login = login;
    }

    public String getPassword() {
        return person.password;
    }

    public void setPassword(String password) {
        person.password = password;
    }

    private Person() {
    }
    public static Person getInstanse()
    {
        return person;
    }

    @Override
    public String toString() {
        return "Person{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
