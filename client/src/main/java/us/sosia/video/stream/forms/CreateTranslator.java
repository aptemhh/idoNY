package us.sosia.video.stream.forms;

import org.jboss.netty.buffer.BigEndianHeapChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;
import us.sosia.video.stream.agent.InterfaceImp;
import us.sosia.video.stream.agent.StreamServerAssept;
import us.sosia.video.stream.server.ConnectorServer;
import us.sosia.video.stream.server.Person;
import us.sosia.video.stream.server.listners.AutorisationListner;
import us.sosia.video.stream.server.listners.CreateTListner;
import us.sosia.video.stream.server.listners.SettingTListner;
import us.sosia.video.stream.server.models.CreateTC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.List;

/**
 * Created by idony on 06.01.17.
 */
public class CreateTranslator  extends JFrame {
    private JPanel panel1;
    private JButton одобритьButton;
    private JButton создатьButton;
    private JButton отключитьButton;
    private JScrollPane scrolpan;
    private Long IdTranslator;
    private List<JCheckBox> jCheckBoxes = new ArrayList<JCheckBox>();

    public CreateTranslator() throws HeadlessException {
        super();
        setContentPane(panel1);
        создатьButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                ConnectorServer connectorServer = ConnectorServer.getInstate();
                try {
//                    ((AutorisationListner) connectorServer.getListner(AutorisationListner.class)).
//                            BisnessLogic(connectorServer, "user","user");
                    CreateTC createTC = ((CreateTListner) connectorServer.getListner(CreateTListner.class)).
                            BisnessLogic(connectorServer, Person.ip, Person.portT);
                    List<String> strings = ((SettingTListner) connectorServer.getListner(SettingTListner.class)).
                            BisnessLogic(connectorServer);

                    JPanel labPanel = new JPanel();
                    labPanel.setVisible(true);
                    labPanel.setForeground(Color.black);
                    scrolpan.add(labPanel);
                    scrolpan.setVisible(true);
                    labPanel.setLayout(new BoxLayout(labPanel, BoxLayout.Y_AXIS));

                    jCheckBoxes.clear();
                    scrolpan.setViewportView(labPanel);
                    for (String s : strings) {
                        JCheckBox label = new JCheckBox(s);
                        label.setAlignmentX(Component.LEFT_ALIGNMENT);
                        label.setFont(new Font("Verdana", Font.PLAIN, 12));
                        labPanel.add(label);
                        labPanel.repaint();
                        jCheckBoxes.add(label);
                        scrolpan.revalidate();
                    }
                    scrolpan.repaint();
                    pack();
                    Person.keyS = createTC.getKey();
                    IdTranslator = createTC.getIdTranslator();
                    InterfaceImp.getInterfaceImp().createServerAssept(new OneToOneDecoder() {
                        @Override
                        protected Object decode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
                            byte[] bytes=((BigEndianHeapChannelBuffer) msg).array();
                            String keyClient = new String(bytes,2,bytes.length-2);
                            if(Person.keyS.equals(keyClient))
                            {
                                System.out.println("Ключ совпал");
                                InterfaceImp.getInterfaceImp().addAsseptAddress (((InetSocketAddress)channel.getRemoteAddress()).getHostName());
                            }
                            else
                                System.out.println("Ключ не совпал");
                            return msg;
                        }
                    });
                    InterfaceImp.getInterfaceImp().startTranslutor(Person.ip);
                } catch (Exception e1) {
                    e1.printStackTrace();
                } finally {
                }
            }
        });
        одобритьButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                ConnectorServer connectorServer = ConnectorServer.getInstate();
                try {
                    List<String> strings = new ArrayList<String>();
                    for (JCheckBox jCheckBox : jCheckBoxes) {
                        if (jCheckBox.isSelected())
                            strings.add(jCheckBox.getText());
                    }
                    ((SettingTListner) connectorServer.getListner(SettingTListner.class)).
                            sendSetting(connectorServer, strings, IdTranslator);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        отключитьButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                InterfaceImp.getInterfaceImp().stopServerTranslator();
                InterfaceImp.getInterfaceImp().stopServerAssept();
            }
        });
    }
}
