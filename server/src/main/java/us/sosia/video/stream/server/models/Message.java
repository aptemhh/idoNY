package us.sosia.video.stream.server.models;

import com.sun.xml.txw2.annotation.XmlAttribute;
import com.sun.xml.txw2.annotation.XmlElement;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.UUID;

/**
 * Created by idony on 01.01.17.
 */
@XmlRootElement
public class Message {
    String type;
    Data data;
    UUID uuid;
    @XmlElement
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
    @XmlElement
    public String getType() {
        return type;
    }

    public Data getData() {
        return data;
    }
    @XmlElement
    public void setData(Data data) {
        this.data = data;
    }

    @XmlAttribute
    public void setType(String type) {
        this.type = type;
    }
    String login;
    String pass;

    public String getLogin() {
        return login;
    }
    @XmlElement
    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }
    @XmlElement
    public void setPass(String pass) {
        this.pass = pass;
    }
}
