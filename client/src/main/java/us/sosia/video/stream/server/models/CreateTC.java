package us.sosia.video.stream.server.models;

import com.sun.xml.txw2.annotation.XmlElement;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by idony on 01.01.17.
 */
@XmlRootElement
public class CreateTC extends Data {
    /**
     * ключ
     */
    String key;
    /**
     * id странслятора
     */
    Long idTranslator;

    public Long getIdTranslator() {
        return idTranslator;
    }

    @XmlElement
    public void setIdTranslator(Long idTranslator) {
        this.idTranslator = idTranslator;
    }

    public String getKey() {
        return key;
    }

    @XmlElement
    public void setKey(String key) {
        this.key = key;
    }
}
