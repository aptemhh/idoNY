package us.sosia.video.stream.server;

/**
 * Created by idony on 06.01.17.
 */
public class Person {
    String login;
    String password;
    static Person person=new Person();

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
