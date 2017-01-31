package org.idony.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by idony on 23.10.16.
 */

@Entity
@Table(name = "USERS")
@NamedQuery(name = "getLogins", query = "select login from org.idony.model.Login l order by login")
public class Login implements Serializable {
    String login;
    String password;

    @Id
    @Column(name = "LOGIN", nullable = false)
    public String getLogin() {
        return login;
    }

    @Column(name = "PASSWORD")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Login(String login) {
        this.login = login;
    }

    public Login() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Login login1 = (Login) o;

        return login != null ? login.equals(login1.login) : login1.login == null;

    }

    @Override
    public int hashCode() {
        return login != null ? login.hashCode() : 0;
    }
}
