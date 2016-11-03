package us.sosia.video.stream.agent;

import com.github.sarxos.webcam.Webcam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.sosia.video.stream.handler.StreamFrameListener;

import java.awt.*;
import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.List;

/**
 * Created by idony on 03.11.16.
 */
public abstract class InterfaceProgramm {
    private static Socket SERVER = null;
    private static ServerSocket LISTENER_KEY;
    private static Dimension dimension = new Dimension(320,240);
    private StreamClientAgent streamClient;
    private StreamServerAgent serverAgent;
    private List<InetSocketAddress> asseptAddressList=new ArrayList<InetSocketAddress>();
    protected final static Logger logger = LoggerFactory.getLogger(InterfaceProgramm.class);

    static
    {
//        try {
//            SERVER = new Socket("2.3.2.3",7778);
//            LISTENER_KEY = new ServerSocket(7777);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void connectToTranslator(StreamFrameListener listener, InetSocketAddress inetSocketAddress)
    {
        streamClient = new StreamClientAgent(listener,dimension);
        streamClient.connect(inetSocketAddress);
        logger.error("Подключаемся к транслятору");
    }

    public void createTranslator(InetSocketAddress inetSocketAddress,Webcam webcam)
    {
        StreamServerAgent serverAgent = new StreamServerAgent(webcam, dimension,asseptAddressList);
        serverAgent.start(inetSocketAddress);
        logger.error("Стартовала трансляция");
    }



    public static Dimension getDimension() {
        return dimension;
    }

    public static void setDimension(Dimension dimension) {
        InterfaceProgramm.dimension = dimension;
    }
}
