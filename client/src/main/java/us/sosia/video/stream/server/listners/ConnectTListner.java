package us.sosia.video.stream.server.listners;

import us.sosia.video.stream.server.ConnectorServer;
import us.sosia.video.stream.server.models.ConnectTC;
import us.sosia.video.stream.server.models.ConnectTS;
import us.sosia.video.stream.server.models.Message;

import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * Created by idony on 04.01.17.
 */
public class ConnectTListner extends MessageListner {


    public Message reader(Message message) {
        if (message == null || message.getUuid() == null || current == null) return null;
        if (message.getData() != null && message.getData() instanceof ConnectTC) {
            if (current.equals(message.getUuid())) {
                this.message = message;
                Notify();
            } else {
                logger.error("UUID не совпадают!!!");
            }
        }
        return null;
    }

    public void BisnessLogic(ConnectorServer connectorServer) throws TimeoutException {

        Message mess = new Message();
        mess.setType(ConnectTS.class.getName());
        mess.setUuid(current = UUID.randomUUID());
        ConnectTS createT = new ConnectTS();
        createT.setLogin("user");
        mess.setLogin("admin");
        mess.setPass("pass1");
        mess.setData(createT);
        connectorServer.write(mess);
        Wait(-1l);
        System.out.println("DOOOOOOOOOOOOOOOOOOOOOD");
    }
}
