package org.idony.listners;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.idony.HibernateUtil;
import org.idony.model.Connector;
import org.idony.model.Login;
import org.idony.model.Translator;
import us.sosia.video.stream.server.models.Message;
import us.sosia.video.stream.server.models.SettingTSO;

/**
 * Created by idony on 05.01.17.
 * обработчик настроек транслятора
 */
public class SettingTOListner implements MessageListner {

    SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public Message reader(Message message) {
        if (message.getData() instanceof SettingTSO) {
            SettingTSO data = (SettingTSO) message.getData();
            Session session = sessionFactory.openSession();
            session.createCriteria(Translator.class).add(Restrictions.eq("login", new Login(message.getLogin()))).
                    add(Restrictions.eq("id", data.getIdTranslator()))
                    .list().size();
            session.close();
            Connector connector;
            for (String s : data.getLogins()) {
                connector = new Connector();
                connector.setLogin(new Login(s));
                connector.setIdTranslator(new Translator(data.getIdTranslator()));
                try {
                    HibernateUtil.save(connector);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
