package us.sosia.video.stream.server.listners;

import us.sosia.video.stream.server.Writter;
import us.sosia.video.stream.server.models.CreateTC;
import us.sosia.video.stream.server.models.CreateTS;
import us.sosia.video.stream.server.models.Message;

import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * Created by idony on 05.01.17.
 * создание трнаслятора
 */
public class CreateTListner extends MessageListner {

    @Override
    Boolean checkMessage(Message message) {
        if (message.getData() != null && message.getType().equals(CreateTC.class.getName()))
            return true;
        return false;
    }

    /**
     *
     * @return CreateTC
     */
    @Override
    Object doAfter() {
        Message message = getMessage();
        return message.getData();
    }

    /**
     *
     * @param objects string ip, integer port
     * @return
     */
    @Override
    Message doBefore(Object[] objects) {
        Message mess = new Message(true);
        mess.setType(CreateTS.class.getName());
        mess.setUuid(current = UUID.randomUUID());
        CreateTS createT = new CreateTS();
        createT.setIp((String)objects[0]);
        createT.setPort((Integer) objects[1]);
        mess.setData(createT);
        return mess;
    }
}
