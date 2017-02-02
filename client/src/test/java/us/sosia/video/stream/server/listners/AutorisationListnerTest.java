package us.sosia.video.stream.server.listners;

import org.junit.Assert;
import us.sosia.video.stream.server.Writter;
import us.sosia.video.stream.server.listners.AutorisationListner;
import us.sosia.video.stream.server.listners.ConnectTListner;
import us.sosia.video.stream.server.models.CreateTC;
import us.sosia.video.stream.server.models.Data;
import us.sosia.video.stream.server.models.Message;

import java.util.concurrent.TimeoutException;


/**
 * Created by idony on 04.01.17.
 */
public class AutorisationListnerTest {
    AutorisationListner autorisationListner = new AutorisationListner();
    @org.junit.Test
    public void test1() throws TimeoutException {
        Writter writter = new WritterImp();
        Assert.assertFalse((Boolean) autorisationListner.BisnessLogic(writter, new Object[]{"test", "test"}));
    }
    @org.junit.Test
    public void test2() throws TimeoutException {
        Writter writter = new WritterImp();
        Assert.assertTrue((Boolean) autorisationListner.BisnessLogic(writter, new Object[]{"test", "test2"}));
    }

    private class WritterImp implements Writter {

        @Override
        public boolean write(Object mess) {
            Message message=(Message)mess;
            if(message.getPass().equals("test"))
            message.setLogin(null);
            message.setData(new Data());
            autorisationListner.reader(message);
            return false;
        }
    }
}