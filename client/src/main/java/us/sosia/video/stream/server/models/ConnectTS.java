package us.sosia.video.stream.server.models;

import javax.sound.sampled.BooleanControl;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by idony on 01.01.17.
 */
@XmlRootElement
public class ConnectTS extends Data {
    String login;

    public String getLogin() {
        return login;
    }
    @XmlElement
    public void setLogin(String login) {
        this.login = login;
    }
}
