package us.sosia.video.stream.server.listners;

import us.sosia.video.stream.server.Writter;
import us.sosia.video.stream.server.models.Message;
import us.sosia.video.stream.server.models.SettingTC;
import us.sosia.video.stream.server.models.SettingTS;
import us.sosia.video.stream.server.models.SettingTSO;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * Created by idony on 05.01.17.
 * настройка транслятора
 */
public class SettingTListner extends MessageListner {


    public Message reader(Message message) {
        if (message == null || message.getUuid() == null || current == null) return null;
        if (message.getData() != null && message.getType().equals(SettingTC.class.getName())) {
            if (current.equals(message.getUuid())) {
                setMessage(message);
                Notify();
            } else {
                logger.error("UUID не совпадают!!!");
            }
        }

        return null;
    }

    public List<String> BisnessLogic(Writter writter) throws TimeoutException {

        Message mess = new Message(true);
        mess.setType(SettingTS.class.getName());
        mess.setUuid(current = UUID.randomUUID());
        SettingTS createT = new SettingTS();
        mess.setData(createT);
        writter.write(mess);

        setMessage(null);

        Wait(-1l);
        for (; getMessage() == null; ) {
            try {
                Thread.sleep(100l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Message message = getMessage();
        setMessage(null);
        return ((SettingTC) message.getData()).getLogins();
    }

    public void sendSetting(Writter writter, List<String> logins, Long idTranslator) {
        Message message = new Message(true);
        message.setUuid(UUID.randomUUID());
        message.setType(SettingTSO.class.getName());
        SettingTSO settingTSO = new SettingTSO();
        settingTSO.setLogins(logins);
        settingTSO.setIdTranslator(idTranslator);
        message.setData(settingTSO);
        writter.write(message);
    }
}

