package us.sosia.video.stream.server.listners;

import us.sosia.video.stream.server.ConnectorServer;
import us.sosia.video.stream.server.models.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * Created by idony on 05.01.17.
 */
public class SettingTListner extends MessageListner {


    public Message reader(Message message) {
        Message mess;
        if (message == null || message.getUuid() == null || current == null) return null;
        if (message.getData() != null && message.getData() instanceof SettingTC) {
            if (current.equals(message.getUuid())) {
                this.message = message;
                Notify();
            } else {
                logger.error("UUID не совпадают!!!");
            }
        }

        return null;
    }

    public List<String> BisnessLogic(ConnectorServer connectorServer) throws TimeoutException {

        Message mess = new Message();
        mess.setType(SettingTS.class.getName());
        mess.setUuid(current = UUID.randomUUID());
        SettingTS createT = new SettingTS();
        mess.setData(createT);
        connectorServer.write(mess);
        Wait(-1l);
        return ((SettingTC)message.getData()).getLogins();
    }
    public void sendSetting(ConnectorServer connectorServer,List<String> logins,Long idTranslator)
    {
        Message message=new Message();
        message.setUuid(UUID.randomUUID());
        message.setType(SettingTSO.class.getName());
        SettingTSO settingTSO=new SettingTSO();
        settingTSO.setLogins(logins);
        settingTSO.setIdTranslator(idTranslator);
        message.setData(settingTSO);
        connectorServer.write(message);
    }
}

