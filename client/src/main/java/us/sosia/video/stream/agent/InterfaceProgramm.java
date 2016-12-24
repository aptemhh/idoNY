package us.sosia.video.stream.agent;

import com.github.sarxos.webcam.Webcam;
import org.jboss.netty.channel.local.LocalAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.sosia.video.stream.agent.ui.ServerAdressAccept;
import us.sosia.video.stream.handler.StreamFrameListener;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
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
    private String keyT="qwe";
    private List<String> asseptAddressList=new ArrayList<String>();
    protected final static Logger logger = LoggerFactory.getLogger(InterfaceProgramm.class);

    public void connectToTranslator(StreamFrameListener listener, InetSocketAddress inetSocketAddress)
    {
        streamClient = new StreamClientAgent(listener,dimension);
        streamClient.connect(inetSocketAddress);
        logger.error("Подключаемся к транслятору");
    }

    public void createTranslator(InetSocketAddress inetSocketAddress,Webcam webcam)
    {
        serverAgent = new StreamServerAgent(webcam, dimension,asseptAddressList);
        serverAgent.start(inetSocketAddress);
        logger.error("Стартовала трансляция");
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
    public void startServerAdressAccept(final ServerAdressAccept serverAdressAccept)
    {
        new Thread(new Runnable() {
            public void run() {
                try {
                    serverAdressAccept.init();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for (;;)
                {
                    try {
                        addAsseptAddress(serverAdressAccept.run(keyT));
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }
    public void connectServerAdressAccept(final InetAddress inetAddress,final String key)
    {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Socket socket=new Socket(inetAddress,15044);
                    new DataOutputStream(socket.getOutputStream()).writeUTF(key);
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }
}
