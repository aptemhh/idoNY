package us.sosia.video.stream.server.models;


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

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getType() {
        return type;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public void setType(String type) {
        this.type = type;
    }
    String login;
    String pass;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
