package org.idony.listners;

import org.idony.HibernateUtil;
import org.idony.model.Login;
import org.idony.model.Translator;
import us.sosia.video.stream.server.models.*;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by idony on 05.01.17.
 */
public class CreateTListner implements MessageListner {

    public Message reader(Message message) {
        Message mess = null;

        if (message.getData() instanceof CreateTS) {
            CreateTS data = (CreateTS) message.getData();

            Translator translator=new Translator();
            translator.setIp(data.getIp());
            translator.setLogin(new Login(message.getLogin()));
            String uuid=UUID.randomUUID().toString();
            translator.setKey(uuid);
            translator.setPort(data.getPort());
            try {
                Serializable serializable= HibernateUtil.save(translator);
                logger.info("Создал транслятор для пользователя:"+message.getLogin()+" ip:port "+data.getIp()+":"+data.getPort());
                mess = new Message();

                mess.setType(CreateTC.class.getName());
                mess.setUuid(message.getUuid());
                CreateTC connectTC = new CreateTC();
                connectTC.setKey(uuid);
                connectTC.setIdTranslator((Long) serializable);
                mess.setData(connectTC);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return mess;
    }
}
