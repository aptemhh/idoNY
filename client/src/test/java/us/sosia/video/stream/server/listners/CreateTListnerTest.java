package us.sosia.video.stream.server.listners;


import org.junit.Assert;
import us.sosia.video.stream.server.Writter;
import us.sosia.video.stream.server.listners.ConnectTListner;
import us.sosia.video.stream.server.listners.CreateTListner;
import us.sosia.video.stream.server.models.*;

import java.util.concurrent.TimeoutException;

/**
 * Created by User on 002 02.02.17.
 */
public class CreateTListnerTest {
    CreateTListner listner = new CreateTListner();
    @org.junit.Test
    public void test1() throws TimeoutException {
        Writter writter = new WritterImp();
        Assert.assertTrue(((CreateTC)listner.BisnessLogic(writter, new Object[]{"123.11",3})).getIdTranslator()==null);
    }
    @org.junit.Test
    public void test2() throws TimeoutException {
        Writter writter = new WritterImp();
        Assert.assertTrue(((CreateTC)listner.BisnessLogic(writter, new Object[]{"0.0.0.0",4})).getIdTranslator()!=null);
    }
    private class WritterImp implements Writter {

        @Override
        public boolean write(Object mess) {
            Message message=(Message)mess;
            CreateTC connectTC=new CreateTC();
            message.setType(CreateTC.class.getName());
            if(((CreateTS)message.getData()).getIp().equals("0.0.0.0"))
            {
                connectTC.setIdTranslator(3l);
                connectTC.setKey("qwefd#wer");
            }
            message.setData(connectTC);
            listner.reader(message);
            return false;
        }
    }
}