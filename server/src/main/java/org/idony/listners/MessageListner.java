package org.idony.listners;

import us.sosia.video.stream.server.models.Message;

/**
 * Created by idony on 04.01.17.
 */
public interface MessageListner {
    Message reader(Message message);
}
