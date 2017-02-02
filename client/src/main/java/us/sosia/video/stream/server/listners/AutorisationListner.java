package us.sosia.video.stream.server.listners;

import us.sosia.video.stream.server.Person;
import us.sosia.video.stream.server.models.Data;
import us.sosia.video.stream.server.models.Message;

import java.util.UUID;

/**
 * Created by idony on 06.01.17.
 * авторизация
 */
public class AutorisationListner extends MessageListner {

    private String login;
    private String pass;

    @Override
    Boolean checkMessage(Message message) {
        if (message.getData() != null && message.getType().equals(Data.class.getName()))
            return true;
        return false;
    }

    /**
     * @return true - проверка успешна
     */
    @Override
    Object doAfter(Message message) {
        Boolean aBoolean = message.getLogin() != null;
        Person person = Person.getInstanse();
        if (aBoolean) {
            person.setLogin(login);
            person.setPassword(pass);
        }
        return aBoolean;
    }

    /**
     * @param objects string логин, пароль
     * @return
     */
    @Override
    Message doBefore(Object[] objects) {
        login = (String) objects[0];
        pass = (String) objects[1];
        Message mess = new Message();
        mess.setType(Data.class.getName());
        mess.setUuid(current = UUID.randomUUID());
        Data createT = new Data();
        mess.setLogin(login);
        mess.setPass(pass);
        mess.setData(createT);
        return mess;
    }
}
