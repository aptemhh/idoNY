package us.sosia.video.stream.forms;

import us.sosia.video.stream.server.ConnectorServer;
import us.sosia.video.stream.server.Person;
import us.sosia.video.stream.server.listners.AutorisationListner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.TimeoutException;

/**
 * Created by idony on 06.01.17.
 */
public class Autorisation extends JFrame {
    private JPanel panel1;
    private JTextField textField1;
    private JButton подключитьсяButton;
    private JPasswordField passwordField1;
    private JLabel Status;

    public Autorisation() throws HeadlessException {
        super();
        setContentPane(panel1);
        подключитьсяButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                try {
                    ConnectorServer connectorServer = ConnectorServer.getInstate();
                    connectorServer.connect();
                    Boolean aBoolean = ((AutorisationListner) connectorServer.getListner(AutorisationListner.class)).
                            BisnessLogic(connectorServer, textField1.getText(), new String(passwordField1.getPassword()));
                    if(aBoolean)
                    {
                        FormManager.hideAll();
                        FormManager.show(form.class);
                    }
                    else
                    {
                        Status.setText("Проверьте лог/пас");
                        Status.setForeground(Color.RED);
                    }
                } catch (TimeoutException e1) {
                    e1.printStackTrace();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                super.mouseClicked(e);
            }
        });
        pack();
    }

}
