package us.sosia.video.stream.server.listners;

import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.sosia.video.stream.server.JAXB;
import us.sosia.video.stream.server.models.Message;

import javax.xml.bind.JAXBException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by idony on 04.01.17.
 */
public class MessageListners {
    Map<Class, MessageListner> listnerArray = new HashMap<Class, MessageListner>();
    protected final static Logger logger = LoggerFactory.getLogger(MessageListners.class);

    /**
     * добавить читатель потока
     *
     * @param name    класс читатаеля
     * @param listner читатель
     */
    public void addListner(Class name, MessageListner listner) {
        listnerArray.put(name, listner);
    }

    /**
     * удалить читатель
     *
     * @param aClass класс читателя
     */
    public void deleteListner(Class aClass) {
        listnerArray.remove(aClass);
    }

    /**
     * @param message
     * @param channelHandlerContext
     */
    public void submitLisners(Message message, ChannelHandlerContext channelHandlerContext) {
        Message message1;
        for (MessageListner messageListner : listnerArray.values()) {
            message1 = messageListner.reader(message);
            if (message1 != null) {
                StringWriter stringWriter = new StringWriter();
                try {
                    JAXB.marshal(stringWriter, message1, Message.class, message1.getData().getClass());
                    channelHandlerContext.write(stringWriter.getBuffer().toString());
                    logger.info("Отправил ответ:\n-----{}-------", stringWriter.getBuffer().toString());
                } catch (JAXBException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    /**
     * вызов листнера с логикой
     *
     * @param name
     * @return
     */
    public MessageListner getListner(Class name) {
        return listnerArray.get(name);
    }
}
