package us.sosia.video.stream.server.models;

import javax.sound.sampled.BooleanControl;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by idony on 01.01.17.
 */
@XmlRootElement
public class ConnectT extends Data {
    Boolean aBoolean;

    public Boolean getaBoolean() {
        return aBoolean;
    }
    @XmlElement
    public void setaBoolean(Boolean aBoolean) {
        this.aBoolean = aBoolean;
    }
}
