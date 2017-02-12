package us.sosia.video.stream.server.listners;

import org.junit.Assert;
import us.sosia.video.stream.server.Writter;
import us.sosia.video.stream.server.models.*;

import java.util.concurrent.TimeoutException;

/**
 * Created by User on 002 02.02.17.
 */
public class SettingTListnerTest {
    MessageListner listner = new SettingTListner();
    @org.junit.Test
    public void test1() throws TimeoutException {
        Assert.assertTrue(((SettingTS)listner.doBefore(new Object[]{}).getData())!=null);
    }
    @org.junit.Test
    public void test2() throws TimeoutException {
        Message message=new Message();
        message.setData(new SettingTC());
        message.setType(SettingTC.class.getName());
        Assert.assertTrue(listner.checkMessage(message));
    }
    @org.junit.Test
    public void test3() throws TimeoutException {
        Message message=new Message();
        message.setData(new SettingTC());
        message.setType(SettingTS.class.getName());
        Assert.assertFalse(listner.checkMessage(message));
    }
    @org.junit.Test
    public void test4() throws TimeoutException {
        Message message=new Message();
        message.setData(new SettingTS());
        message.setType(SettingTC.class.getName());
        Assert.assertTrue(listner.checkMessage(message));
    }
}
