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
 * Created by idony on 06.01.17.
 */
public class AutorisationListner extends MessageListner {


    public Message reader(Message message) {
        if (message == null || message.getUuid() == null || current == null) return null;
        if (message.getData() != null && message.getData() instanceof Data) {
            if (current.equals(message.getUuid())) {
                this.message = message;
                Notify();
            } else {
                logger.error("UUID не совпадают!!!");
            }
        }
        return null;
    }

    public Boolean BisnessLogic(ConnectorServer connectorServer,String login,String pass) throws TimeoutException {

        Message mess = new Message();
        mess.setType(Data.class.getName());
        mess.setUuid(current = UUID.randomUUID());
        Data createT = new Data();
        mess.setLogin(login);
        mess.setPass(pass);
        mess.setData(createT);
        connectorServer.write(mess);
        Wait(-1l);
        Boolean aBoolean=message.getLogin()!=null;
        Person person=Person.getInstanse();
        if(aBoolean){
            person.setLogin(login);
            person.setPassword(pass);
        }
        return aBoolean;
    }
}
