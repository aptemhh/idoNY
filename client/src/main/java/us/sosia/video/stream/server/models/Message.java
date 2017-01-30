package us.sosia.video.stream.server.models;

import com.sun.xml.txw2.annotation.XmlAttribute;
import com.sun.xml.txw2.annotation.XmlElement;
import us.sosia.video.stream.server.Person;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.UUID;

/**
 * Created by idony on 01.01.17.
 */
@XmlRootElement
public class Message {
    /**
     * тип data
     */
    String type;
    /**
     * данные
     */
    Data data;
    /**
     * индификатор сообщения
     */
    UUID uuid;

    public UUID getUuid() {
        return uuid;
    }
    @XmlElement
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

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

    public Message() {
    }

    public Message(boolean lezyInit) {
        if(lezyInit) {
            login = Person.getInstanse().getLogin();
            pass = Person.getInstanse().getPassword();
        }
    }
}
