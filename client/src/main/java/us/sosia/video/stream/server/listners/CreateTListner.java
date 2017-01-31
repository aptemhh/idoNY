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


    public Message reader(Message message) {
        if (message == null || message.getUuid() == null || current == null) return null;
        if (message.getData() != null && message.getType().equals(CreateTC.class.getName())) {
            if (current.equals(message.getUuid())) {
                setMessage(message);
                Notify();
            } else {
                logger.error("UUID не совпадают!!!");
            }
        }
        return null;
    }

    public CreateTC BisnessLogic(Writter writter, String ip, Integer port) throws TimeoutException {

        Message mess = new Message(true);
        mess.setType(CreateTS.class.getName());
        mess.setUuid(current = UUID.randomUUID());
        CreateTS createT = new CreateTS();
        createT.setIp(ip);
        createT.setPort(port);
        mess.setData(createT);
        writter.write(mess);
        Wait(-1l);
        for (; getMessage() == null; ) {
            try {
                Thread.sleep(100l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Message message = getMessage();
        setMessage(null);
        return ((CreateTC) message.getData());
    }
}
