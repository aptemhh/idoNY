package us.sosia.video.stream.server;

import us.sosia.video.stream.server.models.Message;

import java.util.ArrayList;

/**
 * Created by idony on 04.01.17.
 */
public class MessageListners {
    ArrayList<MessageListner> listnerArray=new ArrayList<MessageListner>();

    public void addListner(MessageListner listner)
    {
        listnerArray.add(listner);
    }
    public void deleteListner(MessageListner listner)
    {
        listnerArray.remove(listner);
    }
    public void submitLisners(Message message)
    {
        for(MessageListner messageListner:listnerArray)
        {
            messageListner.reader(message);
        }
    }
}
