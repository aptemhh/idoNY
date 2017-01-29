package us.sosia.video.stream.server.listners;

import us.sosia.video.stream.server.ConnectorServer;
import us.sosia.video.stream.server.Person;
import us.sosia.video.stream.server.models.ConnectTC;
import us.sosia.video.stream.server.models.ConnectTS;
import us.sosia.video.stream.server.models.Data;
import us.sosia.video.stream.server.models.Message;

import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * Created by idony on 04.01.17.
 */
public class ConnectTListner extends MessageListner {


    public Message reader(Message message) {
        if (message == null || message.getUuid() == null || current == null) return null;
        if (message.getData() != null && message.getType().equals(ConnectTC.class.getName())) {
            if (current.equals(message.getUuid())) {
                setMessage(message);
                Notify();
            } else {
                logger.error("UUID не совпадают!!!");
            }
        }
        return null;
    }

    public ConnectTC BisnessLogic(ConnectorServer connectorServer,String loginTranslator) throws TimeoutException {

        Message mess = new Message(true);
        mess.setType(ConnectTS.class.getName());
        mess.setUuid(current = UUID.randomUUID());
        ConnectTS createT = new ConnectTS();
        createT.setLogin(loginTranslator);
        mess.setData(createT);
        connectorServer.write(mess);
        Wait(-1l);
        for(;getMessage()==null;){
            try {
                Thread.sleep(100l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Message message=getMessage();
        setMessage(null);
        return (ConnectTC) message.getData();
    }
}
