package us.sosia.video.stream.forms;

import us.sosia.video.stream.agent.InterfaceImp;
import us.sosia.video.stream.agent.ui.VideoPanel;
import us.sosia.video.stream.server.ConnectorServer;
import us.sosia.video.stream.server.Person;
import us.sosia.video.stream.server.listners.ConnectTListner;
import us.sosia.video.stream.server.models.ConnectTC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.InetSocketAddress;

/**
 * Created by idony on 24.12.16.
 */
public class ClientTranslator extends JFrame {
    private JPanel panel1;
    private JButton button6456tyutyutyutuButton;
    private JTextField textField1;
    private JButton connectTButton;
    private JButton disconnectTButton;
    private JButton audioButton;
    private JButton videoButton;
    private VideoPanel videoPanel = new VideoPanel();

    public ClientTranslator() {
        setContentPane(panel1);
        panel1.add(videoPanel, BorderLayout.CENTER);

        connectTButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                ConnectorServer connectorServer = ConnectorServer.getInstate();
                try {
                    ConnectTC connectTC = (ConnectTC) connectorServer.getListner(ConnectTListner.class).
                            BisnessLogic(connectorServer.getWritter(), new Object[]{"user"});
                    Person.keyP = connectTC.getKey();
                    InterfaceImp.getInterfaceImp().connectServerAdressAccept(new InetSocketAddress(connectTC.getIp(), Person.portP).getAddress(), Person.portP, connectTC.getKey());
                    InterfaceImp.getInterfaceImp().openTranslutor(videoPanel, connectTC.getIp());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        disconnectTButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                InterfaceImp.getInterfaceImp().stopClientTranslator();
            }
        });
        videoButton.addMouseListener(new MouseAdapter() {
            String s = "Вкл. видео", s2 = "Выкл. видео";

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (videoPanel.getVisable()) {
                    videoPanel.setVisable(false);
                    videoButton.setText(s);
                } else {
                    videoPanel.setVisable(true);
                    videoButton.setText(s2);
                }

            }
        });
        audioButton.addMouseListener(new MouseAdapter() {
            String s = "Вкл. звук", s2 = "Выкл. звук";

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (InterfaceImp.getInterfaceImp().getAudio()) {
                    InterfaceImp.getInterfaceImp().setAudio(false);
                    audioButton.setText(s);
                } else {
                    InterfaceImp.getInterfaceImp().setAudio(true);
                    audioButton.setText(s2);
                }
            }
        });
    }

    public static void main(String[] bud) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        ClientTranslator form = new ClientTranslator();
        form.pack();
        form.setVisible(true);
        form.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void createUIComponents() {
    }
}
