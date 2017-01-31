package us.sosia.video.stream;

import us.sosia.video.stream.forms.Autorisation;
import us.sosia.video.stream.forms.ClientTranslator;
import us.sosia.video.stream.forms.CreateTranslator;
import us.sosia.video.stream.forms.FormManager;
import us.sosia.video.stream.server.ConnectorServer;

import javax.swing.*;

/**
 * Created by idony on 06.01.17.
 * Класс запуска форм
 */
public class main {
    public static void main(String[] bud) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        ConnectorServer connectorServer = ConnectorServer.getInstate();
        try {
            connectorServer.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FormManager.add(Autorisation.class);
        FormManager.add(ClientTranslator.class);
        FormManager.add(CreateTranslator.class);
        FormManager.show(Autorisation.class);
    }
}
