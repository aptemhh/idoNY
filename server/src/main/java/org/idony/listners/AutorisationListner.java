package org.idony.listners;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.idony.HibernateUtil;
import org.idony.model.Connector;
import org.idony.model.Login;
import org.idony.model.Translator;
import us.sosia.video.stream.server.models.ConnectTC;
import us.sosia.video.stream.server.models.ConnectTS;
import us.sosia.video.stream.server.models.Data;
import us.sosia.video.stream.server.models.Message;

import java.util.List;

/**
 * Created by idony on 06.01.17.
 */
public class AutorisationListner implements MessageListner {

    public Message reader(Message message) {
        Message mess=null;

        if(message.getType().equals(Data.class.getName()))
        {
            mess = new Message();
            mess.setType(Data.class.getName());
            mess.setUuid(message.getUuid());
            if(HibernateUtil.security(message.getLogin(), message.getPass()))
            {
                mess.setLogin(message.getLogin());
            }

            Data connectTC=new Data();
            mess.setData(connectTC);
        }


        return mess;
    }
}
