package us.sosia.video.stream.server.models;

/**
 * Created by idony on 03.01.17.
 */
public class ConnectTC extends Data {
    String ip;
    Integer port;
    String key;

    public String getIp() {
        return ip;
    }

    /**
     * адресс
     *
     * @param ip
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * порт
     *
     * @return
     */
    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public void setKey(String key) {
        this.key = key;
    }

    /**
     * ключ
     *
     * @return
     */
    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return "ConnectTC{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", key='" + key + '\'' +
                '}';
    }
}
