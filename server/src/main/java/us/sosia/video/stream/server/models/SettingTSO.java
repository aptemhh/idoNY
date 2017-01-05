package us.sosia.video.stream.server.models;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * Created by idony on 05.01.17.
 */
public class SettingTSO  extends Data {
    List<String> logins;
    Long idTranslator;

    @XmlElement(name = "login")
    public List<String> getLogins() {
        return logins;
    }

    public void setLogins(List<String> logins) {
        this.logins = logins;
    }
    @XmlElement
    public Long getIdTranslator() {
        return idTranslator;
    }

    public void setIdTranslator(Long idTranslator) {
        this.idTranslator = idTranslator;
    }
}
