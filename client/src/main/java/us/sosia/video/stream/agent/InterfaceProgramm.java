package us.sosia.video.stream.agent;

import com.github.sarxos.webcam.Webcam;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.sosia.video.stream.handler.StreamFrameListener;
import us.sosia.video.stream.server.Person;

import java.awt.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by idony on 03.11.16.
 */
public abstract class InterfaceProgramm {

    private static Dimension dimension = new Dimension(320, 240);
    private StreamClientAgent streamClient;
    private StreamServerAgent serverAgent;
    private StreamServerAssept serverAssept;
    private StreamServerSoung streamServerSoung;
    private StreamClientSoung streamClientSoung;

    private String keyT = "qwe";
    private List<String> asseptAddressList = new ArrayList<String>();
    protected final static Logger logger = LoggerFactory.getLogger(InterfaceProgramm.class);

    /**
     * Подключиться к видео-трансляции
     *
     * @param listener обработчик видео
     * @param ip       адрес сервера
     */
    public void connectToTranslator(StreamFrameListener listener, String ip) {
        streamClient = new StreamClientAgent(listener, dimension);
        streamClientSoung = new StreamClientSoung();
        streamClientSoung.connect(new InetSocketAddress(ip, Person.portA));
        streamClient.connect(new InetSocketAddress(ip, Person.portT));
        logger.error("Подключаемся к транслятору");
    }

    /**
     * Создать видео-трансляцию
     *
     * @param ip     адрес
     * @param webcam камера,с которой будет вестись видео-поток
     */
    public void createTranslator(String ip, Webcam webcam) {
        serverAgent = new StreamServerAgent(webcam, dimension, asseptAddressList);
        streamServerSoung = new StreamServerSoung(asseptAddressList);
        streamServerSoung.start(new InetSocketAddress(ip, Person.portA));
        streamServerSoung.start();
        serverAgent.start(new InetSocketAddress(ip, Person.portT));
        logger.error("Стартовала трансляция");
    }

    /**
     * Остановить видео-сервер
     */
    public void stopServerTranslator() {
        serverAgent.stop();
        streamServerSoung.stopServer();
        logger.error("Трансляция оставновлена");
    }

    /**
     * Остановить видео-клиент
     */
    public void stopClientTranslator() {
        streamClient.stop();
        streamClientSoung.stop();
        logger.error("Отключен от транслятора");
    }

    /**
     * Добавить одобренный адрес
     *
     * @param asseptAddress одобренный адрес
     */
    public void addAsseptAddress(String asseptAddress) {
        asseptAddressList.add(asseptAddress);
    }

    public void deleteAsseptAddress(final String asseptAddress) {
        asseptAddressList.removeIf(new Predicate<String>() {
            public boolean test(String s) {
                return s.equals(asseptAddress);
            }
        });
    }

    /**
     * отправка ключа одобренному серверу
     *
     * @param inetAddress адрес сервер-одобрения
     * @param port        порт
     * @param key         ключ
     */
    public void connectServerAdressAccept(final InetAddress inetAddress, final Integer port, final String key) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Socket socket = new Socket(inetAddress, port);
                    new DataOutputStream(socket.getOutputStream()).writeUTF(key);
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Включить сервер-одобрения
     *
     * @param oneToOneDecoder обработчик ключей
     */
    public void createServerAssept(OneToOneDecoder oneToOneDecoder) {
        serverAssept = StreamServerAssept.getStreamServerAssept(oneToOneDecoder);
        serverAssept.start(new InetSocketAddress(Person.ip, Person.portP));
    }

    /**
     * остановить сервер-одобрения
     */
    public void stopServerAssept() {
        serverAssept.stop();
    }

    /**
     * вкл\выкл звук
     *
     * @param audio вкл\выкл
     */
    public void setAudio(Boolean audio) {
        streamClientSoung.setAudio(audio);
    }

    /**
     * Включен ли звук
     *
     * @return статус переключателя звука
     */
    public Boolean getAudio() {
        return streamClientSoung.getAudio();
    }


}
