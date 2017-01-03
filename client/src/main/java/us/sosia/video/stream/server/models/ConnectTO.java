package us.sosia.video.stream.server.models;

/**
 * Created by idony on 03.01.17.
 */
public class ConnectTO extends Data {
    String ip;
    Integer port;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
