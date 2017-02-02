package us.sosia.video.stream.server.listners;

import us.sosia.video.stream.server.Writter;
import us.sosia.video.stream.server.models.Message;
import us.sosia.video.stream.server.models.SettingTC;
import us.sosia.video.stream.server.models.SettingTS;
import us.sosia.video.stream.server.models.SettingTSO;

import java.util.List;
import java.util.UUID;

/**
 * Created by idony on 05.01.17.
 * настройка транслятора
 */
public class SettingTListner extends MessageListner {


    @Override
    Boolean checkMessage(Message message) {
        if (message.getData() != null && message.getType().equals(SettingTC.class.getName()))
            return true;
        return false;
    }

    @Override
    Object doAfter(Message message) {
        return ((SettingTC) message.getData()).getLogins();
    }

    /**
     * @param objects пустой
     * @return
     */
    @Override
    Message doBefore(Object[] objects) {
        Message mess = new Message(true);
        mess.setType(SettingTS.class.getName());
        mess.setUuid(current = UUID.randomUUID());
        SettingTS createT = new SettingTS();
        mess.setData(createT);
        return mess;
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

