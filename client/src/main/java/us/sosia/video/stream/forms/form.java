package us.sosia.video.stream.forms;

import us.sosia.video.stream.agent.InterfaceImp;
import us.sosia.video.stream.agent.ui.ServerAdressAccept;
import us.sosia.video.stream.agent.ui.VideoPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by idony on 24.12.16.
 */
public class form extends JFrame{
    private JPanel panel1;
    private JButton fghfghfghButton;
    private JButton button6456tyutyutyutuButton;
    private VideoPanel videoPanel=new VideoPanel();
    private InterfaceImp imp=new InterfaceImp();

    public form(){
        setContentPane(panel1);
        panel1.add(videoPanel, BorderLayout.CENTER);
        fghfghfghButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);

                imp.startServerAdressAccept(new ServerAdressAccept() {
                    ServerSocket serverSocket;
                    public void init() throws IOException {
                        serverSocket=new ServerSocket(15044);
                    }

                    public String run(String key) {
                        Socket client=null;
                        String keyClient=null;
                        try {
                            client= serverSocket.accept();
                            keyClient=new DataInputStream(client.getInputStream()).readUTF();

                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        if(key.equals(keyClient))
                        {
                            System.out.println("Ключ совпал");
                            return client.getLocalAddress().getCanonicalHostName();
                        }
                        else
                            System.out.println("Ключ не совпал");
                        return "";
                    }
                });
                imp.startTranslutor();
            }
        });
        button6456tyutyutyutuButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                try {
                    imp.connectServerAdressAccept(NetworkInterface.getNetworkInterfaces().nextElement().getInetAddresses().nextElement(),"qwe");
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
