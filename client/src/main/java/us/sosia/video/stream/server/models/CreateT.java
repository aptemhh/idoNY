package us.sosia.video.stream.server.models;

import com.sun.xml.txw2.annotation.XmlElement;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by idony on 01.01.17.
 */
@XmlRootElement
public class CreateT extends Data{
    String ip;

    public String getIp() {
        return ip;
    }
    @XmlElement
    public void setIp(String ip) {
        this.ip = ip;
    }
}
