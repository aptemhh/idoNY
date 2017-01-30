package org.idony.listners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.sosia.video.stream.server.models.Message;

/**
 * Created by idony on 04.01.17.
 * интерфейс обработчиков
 */
public interface MessageListner {
    final static Logger logger = LoggerFactory.getLogger(MessageListner.class);
    Message reader(Message message);
}
