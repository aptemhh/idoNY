package org.idony.listners;

import us.sosia.video.stream.server.models.ConnectT;
import us.sosia.video.stream.server.models.CreateT;
import us.sosia.video.stream.server.models.Message;

import java.util.UUID;

/**
 * Created by idony on 04.01.17.
 */
public class ConnectTListner implements MessageListner {


    public Message reader(Message message) {
        Message mess=null;


        if(message.getData() instanceof CreateT)
        {
            mess = new Message();
            mess.setType(ConnectT.class.getName());
            mess.setUuid(message.getUuid());
            ConnectT createT = new ConnectT();

            mess.setLogin("login1");
            mess.setPass("pass1");
            mess.setData(createT);
        }


        return mess;
    }
}
