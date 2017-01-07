package us.sosia.video.stream.forms;

import us.sosia.video.stream.agent.InterfaceImp;
import us.sosia.video.stream.agent.ui.VideoPanel;
import us.sosia.video.stream.server.ConnectorServer;
import us.sosia.video.stream.server.Person;
import us.sosia.video.stream.server.listners.AutorisationListner;
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
public class ClientTranslator extends JFrame{
    private JPanel panel1;
    private JButton button6456tyutyutyutuButton;
    private JTextField textField1;
    private JButton подключитьсяButton;
    private JButton отключитьсяButton;
    private JButton отклЗвукButton;
    private JButton отклВидеоButton;
    private VideoPanel videoPanel=new VideoPanel();

    public ClientTranslator(){
        setContentPane(panel1);
        panel1.add(videoPanel, BorderLayout.CENTER);

        подключитьсяButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                ConnectorServer connectorServer = ConnectorServer.getInstate();
                try {
                    ((AutorisationListner) connectorServer.getListner(AutorisationListner.class)).
                            BisnessLogic(connectorServer, "clop","clop");

                    ConnectTC connectTC= ((ConnectTListner)connectorServer.getListner(ConnectTListner.class)).BisnessLogic(connectorServer,"user");
                    Person.keyP=connectTC.getKey();

                    InterfaceImp.getInterfaceImp().connectServerAdressAccept(new InetSocketAddress(connectTC.getIp(),Person.portP).getAddress(),Person.portP,connectTC.getKey());
                    InterfaceImp.getInterfaceImp().openTranslutor(videoPanel, connectTC.getIp(), connectTC.getPort());

                }  catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        отключитьсяButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                InterfaceImp.getInterfaceImp().stopClientTranslator();
            }
        });
        отклВидеоButton.addMouseListener(new MouseAdapter() {
            String s="Вкл. видео",s2="Выкл. видео";

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(videoPanel.getVisable())
                {
                    videoPanel.setVisable(false);
                    отклВидеоButton.setText(s);
                }
                else {
                    videoPanel.setVisable(true);
                    отклВидеоButton.setText(s2);
                }

            }
        });
    }

    public static void main(String[] bud) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        ClientTranslator form=new ClientTranslator();
        form.pack();
        form.setVisible(true);
        form.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    private void createUIComponents() {


    }
}
