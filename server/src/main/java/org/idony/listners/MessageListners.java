package org.idony.listners;

import io.netty.channel.ChannelHandlerContext;
import org.idony.HibernateUtil;
import org.idony.JAXB;
import org.idony.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.sosia.video.stream.server.models.Data;
import us.sosia.video.stream.server.models.Message;

import javax.xml.bind.JAXBException;
import java.io.StringWriter;
import java.util.ArrayList;

/**
 * Created by idony on 04.01.17.
 */
public class MessageListners {
    ArrayList<MessageListner> listnerArray=new ArrayList<MessageListner>();
    protected final static Logger logger = LoggerFactory.getLogger(MessageListners.class);
    public void addListner(MessageListner listner)
    {
        listnerArray.add(listner);
    }
    public void deleteListner(MessageListner listner)
    {
        listnerArray.remove(listner);
    }
    public void submitLisners(Message message,ChannelHandlerContext channelHandlerContext)
    {
        if(!HibernateUtil.security(message.getLogin(), message.getPass())&&!(message.getType().equals(Data.class.getName())))
            return;
        Message message1;
        for(MessageListner messageListner:listnerArray)
        {
            message1= messageListner.reader(message);
            if(message1!=null)
            {
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
}
