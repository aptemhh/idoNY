package us.sosia.video.stream.forms;

import org.jboss.netty.buffer.BigEndianHeapChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;
import us.sosia.video.stream.agent.InterfaceImp;
import us.sosia.video.stream.agent.StreamServerAssept;
import us.sosia.video.stream.agent.ui.VideoPanel;
import us.sosia.video.stream.server.ConnectorServer;
import us.sosia.video.stream.server.listners.AutorisationListner;
import us.sosia.video.stream.server.listners.ConnectTListner;
import us.sosia.video.stream.server.listners.CreateTListner;
import us.sosia.video.stream.server.listners.SettingTListner;
import us.sosia.video.stream.server.models.ConnectTC;
import us.sosia.video.stream.server.models.CreateTC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.*;
import java.util.List;

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
                new Thread(new Runnable() {
                    public void run() {
                        String ip=null;
                        try {
                            ip=NetworkInterface.getNetworkInterfaces().nextElement().getInetAddresses().nextElement().getCanonicalHostName();
                        } catch (SocketException e1) {
                            e1.printStackTrace();
                        }
                        Integer portT= 20000;
                        Integer portS= 20001;
                        Integer portP= 15044;
                        ConnectorServer connectorServer = ConnectorServer.getInstate();
                        try {
                            connectorServer.connect(ip, portS);
                            Boolean aBoolean = ((AutorisationListner)connectorServer.getListner(AutorisationListner.class)).
                                    BisnessLogic(connectorServer, "user", "user");
                            CreateTC createTC = ((CreateTListner)connectorServer.getListner(CreateTListner.class)).
                                    BisnessLogic(connectorServer,ip,portT);
                            List<String> strings = ((SettingTListner)connectorServer.getListner(SettingTListner.class)).
                                    BisnessLogic(connectorServer);
                            strings.remove(0);
                            ((SettingTListner)connectorServer.getListner(SettingTListner.class)).
                                    sendSetting(connectorServer, strings, createTC.getIdTranslator());
                            keyServ=createTC.getKey();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }

                        //region сервер предодобрения
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

                        ).start(new InetSocketAddress(ip,portP));

                        //endregion

                        imp.startTranslutor(ip,portT);
                    }
                }).start();
            }
        });
        button6456tyutyutyutuButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                try {
                    InetAddress ip=null;
                    try {
                        ip=NetworkInterface.getNetworkInterfaces().nextElement().getInetAddresses().nextElement();
                    } catch (SocketException e1) {
                        e1.printStackTrace();
                    }
                    Integer portP= 15044;
                    Integer portS= 20001;

                    ConnectorServer connectorServer = ConnectorServer.getInstate();
                    connectorServer.connect(ip.getCanonicalHostName(), portS);
                    Boolean aBoolean = ((AutorisationListner)connectorServer.getListner(AutorisationListner.class)).
                            BisnessLogic(connectorServer, "clop", "clop");
                    ConnectTC connectTC= ((ConnectTListner)connectorServer.getListner(ConnectTListner.class)).BisnessLogic(connectorServer,"user");
                    connectorServer.stop();

                    imp.connectServerAdressAccept(ip,portP,connectTC.getKey());
                    imp.openTranslutor(videoPanel, connectTC.getIp(), connectTC.getPort());

                } catch (SocketException e1) {
                    e1.printStackTrace();
                } catch (Exception e1) {
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
