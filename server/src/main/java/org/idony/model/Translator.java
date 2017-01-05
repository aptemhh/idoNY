package org.idony.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by idony on 05.01.17.
 */
@Entity
@Table(name = "translator")
public class Translator implements Serializable {
    public Translator(Long id) {
        this.id = id;
    }

    Long id;

    public Translator() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    String ip;
    Integer port;
    String key;
    Login login;
    List<Connector> connectors;

    public String getIp() {
        return ip;
    }

    public Integer getPort() {
        return port;
    }

    public String getKey() {
        return key;
    }
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "LOGIN")
    public Login getLogin() {
        return login;
    }

    @OneToMany(fetch=FetchType.EAGER)
    @JoinColumn(name = "ID_TRANSLATOR")
    public List<Connector> getConnectors() {
        return connectors;
    }

    public void setConnectors(List<Connector> connectors) {
        this.connectors = connectors;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Translator that = (Translator) o;

        return id != null ? id.equals(that.id) : that.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
