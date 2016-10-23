package org.idony.model;
import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by idony on 23.10.16.
 */

@Entity
@Table(name = "AUTHORIZATION")
@NamedQuery(name = "getLogin",query = "select l from org.idony.model.Login l")
public class Login implements Serializable {
    String login;
    String password;

    @Id
    @Column (name = "LOGIN",nullable = false)
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
}
