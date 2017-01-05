package us.sosia.video.stream.server.models;



import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import java.util.List;

/**
 * Created by idony on 05.01.17.
 */
public class SettingTC  extends Data {
    List<String> logins;


    @XmlElement(name = "login")
    public List<String> getLogins() {
        return logins;
    }

    public void setLogins(List<String> logins) {
        this.logins = logins;
    }
}
