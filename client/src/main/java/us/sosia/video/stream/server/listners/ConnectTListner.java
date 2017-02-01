package us.sosia.video.stream.server.listners;

import us.sosia.video.stream.server.Writter;
import us.sosia.video.stream.server.models.ConnectTC;
import us.sosia.video.stream.server.models.ConnectTS;
import us.sosia.video.stream.server.models.Message;

import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * Created by idony on 04.01.17.
 * получение реквезитов транслятора
 */
public class ConnectTListner extends MessageListner {

    @Override
    Boolean checkMessage(Message message) {
        if (message.getData() != null && message.getType().equals(ConnectTC.class.getName()))
            return true;
        return false;
    }

    @Override
    Object doAfter() {
        Message message = getMessage();
        return message.getData();
    }

    /**
     *
     * @param objects string логин, к которому хотим подключиться
     * @return
     */
    @Override
    Message doBefore(Object[] objects) {
        Message mess = new Message(true);
        mess.setType(ConnectTS.class.getName());
        mess.setUuid(current = UUID.randomUUID());
        ConnectTS createT = new ConnectTS();
        createT.setLogin((String)objects[0]);
        mess.setData(createT);
        return mess;
    }
}
