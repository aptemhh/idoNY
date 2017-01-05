package us.sosia.video.stream.server.listners;

import us.sosia.video.stream.server.ConnectorServer;
import us.sosia.video.stream.server.models.CreateTC;
import us.sosia.video.stream.server.models.CreateTS;
import us.sosia.video.stream.server.models.Message;

import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * Created by idony on 05.01.17.
 */
public class CreateTListner extends MessageListner {


    public Message reader(Message message) {
        if (message == null || message.getUuid() == null || current == null) return null;
        if (message.getData() != null && message.getData() instanceof CreateTC) {
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
        mess.setType(CreateTS.class.getName());
        mess.setUuid(current = UUID.randomUUID());
        CreateTS createT = new CreateTS();
        createT.setIp("123");
        createT.setPort(123);
        mess.setLogin("admin");
        mess.setPass("admin");
        mess.setData(createT);
        connectorServer.write(mess, Message.class, CreateTS.class);
        Wait(-1l);
        System.out.println("DOOOOOOOOOOOOOOOOOOOOOD");
    }
}
