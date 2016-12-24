package us.sosia.video.stream.agent;

import com.github.sarxos.webcam.Webcam;
import us.sosia.video.stream.agent.ui.VideoPanel;
import us.sosia.video.stream.handler.StreamFrameListener;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;

/**
 * Created by idony on 03.11.16.
 */
public class InterfaceImp extends InterfaceProgramm {
    private final static Dimension dimension = new Dimension(320, 240);
    protected  VideoPanel videoPannel;


    public InterfaceImp() {
        super();

    }

    public void init() {

    }

    public void openTranslutor(VideoPanel videoPanel, String inetAddress, Integer port) {

            this.videoPannel = videoPanel;
            this.videoPannel.setPreferredSize(dimension);
            connectToTranslator(new StreamFrameListener() {
                public void onFrameReceived(BufferedImage image) {
                    videoPannel.updateImage(image);
                }
            }, new InetSocketAddress(inetAddress,port));

    }

    public void startTranslutor() {
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