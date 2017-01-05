package org.idony.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by idony on 05.01.17.
 */
@Entity
@Table(name = "connection")
public class Connector  implements Serializable {
    private Long id;
    Login login;
    Translator idTranslator;

    @ManyToOne
    @JoinColumn(name = "LOGIN")
    public Login getLogin() {
        return login;
    }
    @ManyToOne
    @JoinColumn(name = "ID_TRANSLATOR")
    public Translator getIdTranslator() {
        return idTranslator;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public void setIdTranslator(Translator idTranslator) {
        this.idTranslator = idTranslator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Connector connector = (Connector) o;

        return id != null ? id.equals(connector.id) : connector.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
