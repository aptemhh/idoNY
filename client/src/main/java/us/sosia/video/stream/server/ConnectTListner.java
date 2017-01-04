package us.sosia.video.stream.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.sosia.video.stream.server.models.ConnectT;
import us.sosia.video.stream.server.models.CreateT;
import us.sosia.video.stream.server.models.Message;

import java.io.StringWriter;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * Created by idony on 04.01.17.
 */
public class ConnectTListner extends MessageListner {


    public void reader(Message message) {

        if(message.getData()!=null&&message.getData() instanceof ConnectT)
        {
            if(current.equals(message.getUuid()))
                Notify();
            else
            {
                logger.error("UUID не совпадают!!!");
            }
        }

    }

    public void BisnessLogic(ConnectorServer connectorServer) throws TimeoutException {

        Message mess = new Message();
        mess.setType(CreateT.class.getName());
        mess.setUuid(current=UUID.randomUUID());
        CreateT createT = new CreateT();
        createT.setIp("1..2.3.3");
        mess.setLogin("login1");
        mess.setPass("pass1");
        mess.setData(createT);
        connectorServer.write(mess,Message.class,CreateT.class);
        Wait(-1l);
        System.out.println("DOOOOOOOOOOOOOOOOOOOOOD");
    }
}
