package org.idony.listners;

import org.idony.HibernateUtil;
import us.sosia.video.stream.server.models.Data;
import us.sosia.video.stream.server.models.Message;

/**
 * Created by idony on 06.01.17.
 * обработчик авторизации
 */
public class AutorisationListner implements MessageListner {

    public Message reader(Message message) {
        Message mess = null;
        if (message.getType().equals(Data.class.getName())) {
            mess = new Message();
            mess.setType(Data.class.getName());
            mess.setUuid(message.getUuid());
            if (HibernateUtil.security(message.getLogin(), message.getPass())) {
                mess.setLogin(message.getLogin());
            }
            Data connectTC = new Data();
            mess.setData(connectTC);
        }
        return mess;
    }
}
