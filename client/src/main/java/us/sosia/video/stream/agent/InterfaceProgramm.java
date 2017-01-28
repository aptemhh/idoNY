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
import java.net.*;
import java.util.*;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by idony on 03.11.16.
 */
public abstract class InterfaceProgramm {

    private static Dimension dimension = new Dimension(320,240);
    private StreamClientAgent streamClient;
    private StreamServerAgent serverAgent;
    private StreamServerAssept serverAssept;
    private StreamServerSoung streamServerSoung;
    private StreamClientSoung streamClientSoung;

    private String keyT="qwe";
    private List<String> asseptAddressList=new ArrayList<String>();
    protected final static Logger logger = LoggerFactory.getLogger(InterfaceProgramm.class);

    public void connectToTranslator(StreamFrameListener listener, String  ip)
    {
        streamClient = new StreamClientAgent(listener,dimension);
        streamClientSoung=new StreamClientSoung();
        streamClientSoung.connect(new InetSocketAddress(ip,Person.portA));
        streamClient.connect(new InetSocketAddress(ip,Person.portT));
        logger.error("Подключаемся к транслятору");
    }

    public void createTranslator(String ip,Webcam webcam)
    {
        serverAgent = new StreamServerAgent(webcam, dimension,asseptAddressList);
        streamServerSoung=new StreamServerSoung(asseptAddressList);
        streamServerSoung.start(new InetSocketAddress(ip,Person.portA));
        streamServerSoung.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        serverAgent.start(new InetSocketAddress(ip,Person.portT));
        logger.error("Стартовала трансляция");
    }
    public void stopServerTranslator()
    {
        serverAgent.stop();
        streamServerSoung.stopServer();
        logger.error("Трансляция оставновлена");
    }
    public void stopClientTranslator()
    {
        streamClient.stop();
        streamClientSoung.stop();
        logger.error("Отключен от транслятора");
    }


    public static Dimension getDimension() {
        return dimension;
    }

    public static void setDimension(Dimension dimension) {
        InterfaceProgramm.dimension = dimension;
    }
    public void addAsseptAddress(String asseptAddress)
    {
        asseptAddressList.add(asseptAddress);
    }
    public void deleteAsseptAddress(final String asseptAddress)
    {
        asseptAddressList.removeIf(new Predicate<String>() {
            public boolean test(String s) {
                return s.equals(asseptAddress);
            }
        });
    }

    public void connectServerAdressAccept(final InetAddress inetAddress, final Integer port, final String key)
    {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Socket socket=new Socket(inetAddress ,port);
                    new DataOutputStream(socket.getOutputStream()).writeUTF(key);
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void createServerAssept(OneToOneDecoder oneToOneDecoder)
    {
        serverAssept=StreamServerAssept.getStreamServerAssept(oneToOneDecoder);
        serverAssept.start(new InetSocketAddress(Person.ip,Person.portP));
    }
    public void stopServerAssept()
    {
        serverAssept.stop();
    }
    public void setAudio(Boolean audio)
    {
        streamClientSoung.setAudio(audio);
    }
    public Boolean getAudio()
    {
        return streamClientSoung.getAudio();
    }


}
