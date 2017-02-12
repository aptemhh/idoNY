package us.sosia.video.stream.server.listners;

import org.junit.Assert;
import us.sosia.video.stream.server.Writter;
import us.sosia.video.stream.server.models.ConnectTC;
import us.sosia.video.stream.server.models.ConnectTS;
import us.sosia.video.stream.server.models.Message;

import java.util.concurrent.TimeoutException;


/**
 * Created by idony on 04.01.17.
 */
public class ConnectTListnerTest {
    ConnectTListner listner = new ConnectTListner();

    @org.junit.Test
    public void test1() throws TimeoutException {
        Writter writter = new WritterImp();
        Assert.assertTrue(((ConnectTC) listner.BisnessLogic(writter, new Object[]{"test"})).getIp() != null);
    }

    @org.junit.Test
    public void test2() throws TimeoutException {
        Writter writter = new WritterImp();
        Assert.assertTrue(((ConnectTC) listner.BisnessLogic(writter, new Object[]{"test2"})).getIp() == null);
    }

    private class WritterImp implements Writter {

        @Override
        public boolean write(Object mess) {
            Message message = (Message) mess;
            ConnectTC connectTC = new ConnectTC();
            message.setType(ConnectTC.class.getName());
            if (((ConnectTS) message.getData()).getLogin().equals("test")) {
                connectTC.setIp("123.123.123");
            }
            message.setData(connectTC);
            listner.reader(message);
            return false;
        }
    }
}