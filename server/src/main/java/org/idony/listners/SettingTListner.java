package org.idony.listners;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.idony.HibernateUtil;
import org.idony.model.Connector;
import org.idony.model.Login;
import org.idony.model.Translator;
import us.sosia.video.stream.server.models.*;

import java.util.List;

/**
 * Created by idony on 05.01.17.
 */
public class SettingTListner implements MessageListner {

    SessionFactory sessionFactory= HibernateUtil.getSessionFactory();
    public Message reader(Message message) {
        Message mess=null;

        if(message.getData() instanceof SettingTS)
        {

            Session session=sessionFactory.openSession();
            List<String> logins=
                    session.createCriteria(Login.class).setProjection( Projections.property( "login" )).list();

            mess = new Message();
            if(logins.size()==0)return mess;

            mess.setType(SettingTC.class.getName());
            mess.setUuid(message.getUuid());
            SettingTC connectTC=new SettingTC();
            connectTC.setLogins(logins);
            mess.setData(connectTC);
        }


        return mess;
    }
}

