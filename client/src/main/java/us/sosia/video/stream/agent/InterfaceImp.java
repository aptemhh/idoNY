package us.sosia.video.stream.agent;

import com.github.sarxos.webcam.Webcam;
import us.sosia.video.stream.agent.ui.VideoPanel;
import us.sosia.video.stream.handler.StreamFrameListener;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;

/**
 * Created by idony on 03.11.16.
 */
public class InterfaceImp extends InterfaceProgramm {
    private final static Dimension dimension = new Dimension(320, 240);
    protected final VideoPanel videoPannel;
    protected final JFrame window;

    public InterfaceImp() {
        super();
        this.window = new JFrame("dfsdf");
        this.videoPannel = new VideoPanel();

        this.videoPannel.setPreferredSize(dimension);
        this.window.add(videoPannel);
        this.window.pack();
        this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void init() {

    }

    public void startCl() {
        try {
            window.setVisible(true);
            connectToTranslator(new StreamFrameListener() {
                private volatile long count = 0;

                public void onFrameReceived(BufferedImage image) {
                    videoPannel.updateImage(image);
                }
            }, new InetSocketAddress(NetworkInterface.getNetworkInterfaces().nextElement().getInetAddresses().nextElement().getCanonicalHostName(), 20000));
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void startSer() {
        try {
            Webcam.setAutoOpenMode(true);
            Webcam webcam = Webcam.getDefault();
            Dimension dimension = new Dimension(320, 240);
            webcam.setViewSize(dimension);
            if (webcam == null)
                System.out.println("err");

            createTranslator(new InetSocketAddress(NetworkInterface.getNetworkInterfaces().nextElement().getInetAddresses().nextElement().getCanonicalHostName(), 20000), webcam);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}