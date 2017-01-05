package org.idony.listners;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.idony.HibernateUtil;
import org.idony.model.Connector;
import org.idony.model.Login;
import org.idony.model.Translator;
import us.sosia.video.stream.server.models.ConnectTC;
import us.sosia.video.stream.server.models.ConnectTS;
import us.sosia.video.stream.server.models.Message;

import java.util.List;

/**
 * Created by idony on 04.01.17.
 */
public class ConnectTListner implements MessageListner {

    SessionFactory sessionFactory=HibernateUtil.getSessionFactory();
    public Message reader(Message message) {
        Message mess=null;

        if(message.getData() instanceof ConnectTS)
        {
            ConnectTS data= (ConnectTS) message.getData();


            Session session=sessionFactory.openSession();
            List<Connector> connectors=
            session.createCriteria(Connector.class).add(Restrictions.eq("login", new Login(message.getLogin()))).
                    createCriteria("idTranslator").add(Restrictions.eq("login", new Login(data.getLogin())))
                    .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();

            mess = new Message();
            if(connectors.size()==0)return mess;
            Translator translator=connectors.get(0).getIdTranslator();
            mess.setType(ConnectTC.class.getName());
            mess.setUuid(message.getUuid());
            ConnectTC connectTC=new ConnectTC();
            connectTC.setIp(translator.getIp());
            connectTC.setPort(translator.getPort());
            connectTC.setKey(translator.getKey());
            mess.setData(connectTC);
        }


        return mess;
    }
}
