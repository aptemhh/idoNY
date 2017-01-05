package us.sosia.video.stream.server.listners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.sosia.video.stream.server.models.Message;

import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * Created by idony on 04.01.17.
 */
public abstract class MessageListner {
    abstract Message reader(Message message);

    protected final static Logger logger = LoggerFactory.getLogger(MessageListner.class);
    static UUID current = null;
    static final Object monitor = new Object();
    Message message;

    public void Notify() {
        synchronized (monitor) {
            monitor.notify();
            current = null;
        }
    }

    int i = 0;

    public void Wait(long l) throws TimeoutException {
        synchronized (monitor) {
            i = 0;
            while (current != null) {
                try {
                    if (l < 0) monitor.wait();
                    else
                        monitor.wait(l);

                    i++;
                    if (i > 40) throw new TimeoutException();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
