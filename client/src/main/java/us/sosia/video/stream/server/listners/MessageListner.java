package us.sosia.video.stream.server.listners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.sosia.video.stream.server.Writter;
import us.sosia.video.stream.server.models.Message;

import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * Created by idony on 04.01.17.
 */
public abstract class MessageListner {

    static UUID current = null;
    static final Object monitor = new Object();
    private volatile Message message;
    int i = 0;

    abstract Boolean checkMessage(Message message);

    abstract Object doAfter();

    abstract Message doBefore(Object[] objects);

    Message answerMessage(Message message) {
        return null;
    }

    public void Notify() {
        synchronized (monitor) {
            current = null;
            monitor.notify();
        }
    }

    public void Wait(long l) throws TimeoutException {
        synchronized (monitor) {
            i = 0;
            while (current != null || message == null) {
                try {
                    if (l < 0) monitor.wait();
                    else
                        monitor.wait(l);
                    if (i > 40) throw new TimeoutException();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public synchronized Message getMessage() {
        return message;
    }

    public synchronized void setMessage(Message message) {
        this.message = message;
    }

    public Object BisnessLogic(Writter writter, Object[] objects) throws TimeoutException {
        writter.write(doBefore(objects));
        Wait(-1l);
        Object o=doAfter();
        setMessage(null);
        return o;
    }

    public Message reader(Message message) {
        if (message == null || message.getUuid() == null || current == null) return null;
        if (current.equals(message.getUuid()) && checkMessage(message)) {
            setMessage(message);
            Notify();
        } else {
            //logger.error("UUID не совпадают!!!");
        }
        return answerMessage(message);
    }
}
