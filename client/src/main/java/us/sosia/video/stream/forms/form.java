package us.sosia.video.stream.forms;

import org.jboss.netty.buffer.BigEndianHeapChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;
import us.sosia.video.stream.agent.InterfaceImp;
import us.sosia.video.stream.agent.StreamServerAssept;
import us.sosia.video.stream.agent.ui.VideoPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.*;

/**
 * Created by idony on 24.12.16.
 */
public class form extends JFrame{
    private JPanel panel1;
    private JButton fghfghfghButton;
    private JButton button6456tyutyutyutuButton;
    private VideoPanel videoPanel=new VideoPanel();
    private InterfaceImp imp=new InterfaceImp();
    private String keyServ="755423424dasd";
    private String keyClient="755423424dasd";
    public form(){
        setContentPane(panel1);
        panel1.add(videoPanel, BorderLayout.CENTER);
        fghfghfghButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                //region сервер предодобрения
                try {
                    new StreamServerAssept(new OneToOneDecoder() {
                        @Override
                        protected Object decode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
                            byte[] bytes=((BigEndianHeapChannelBuffer) msg).array();
                            String keyClient = new String(bytes,2,bytes.length-2);
                            if(keyServ.equals(keyClient))
                        {
                            System.out.println("Ключ совпал");
                            imp.addAsseptAddress (((InetSocketAddress)channel.getRemoteAddress()).getHostName());
                        }
                        else
                            System.out.println("Ключ не совпал");
                            return msg;
                        }
                    }

                    ).start(new InetSocketAddress(NetworkInterface.getNetworkInterfaces().nextElement().getInetAddresses().nextElement().getCanonicalHostName(),15044));
                } catch (SocketException e1) {
                    e1.printStackTrace();
                }
                //endregion

                imp.startTranslutor();
            }
        });
        button6456tyutyutyutuButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                try {
                    imp.connectServerAdressAccept(NetworkInterface.getNetworkInterfaces().nextElement().getInetAddresses().nextElement(),keyClient);
                    imp.openTranslutor(videoPanel, NetworkInterface.getNetworkInterfaces().nextElement().getInetAddresses().nextElement().getCanonicalHostName(), 20000);

                } catch (SocketException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] bud) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        form form=new form();
        form.pack();
        form.setVisible(true);
        form.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    private void createUIComponents() {


    }
}
