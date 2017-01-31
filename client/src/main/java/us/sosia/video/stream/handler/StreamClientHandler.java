package us.sosia.video.stream.handler;

import org.jboss.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StreamClientHandler extends SimpleChannelHandler {
    protected final StreamClientListener streamClientListener;
    protected final static Logger logger = LoggerFactory.getLogger(StreamClientHandler.class);

    public StreamClientHandler(StreamClientListener streamClientListener) {
        super();
        this.streamClientListener = streamClientListener;
    }

    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
            throws Exception {
        Channel channel = e.getChannel();
        Throwable t = e.getCause();
        logger.debug("exception at :{}", channel);
        streamClientListener.onException(channel, t);
        //super.exceptionCaught(ctx, e);
    }

    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
            throws Exception {
        Channel channel = e.getChannel();
        logger.info("channel connected at {}", channel);
        streamClientListener.onConnected(channel);
        super.channelConnected(ctx, e);
    }

    public void channelDisconnected(ChannelHandlerContext ctx,
                                    ChannelStateEvent e) throws Exception {
        Channel channel = e.getChannel();
        logger.info("channel disconnected at :{}", channel);
        streamClientListener.onDisconnected(channel);
        super.channelDisconnected(ctx, e);
    }

    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
            throws Exception {
        super.messageReceived(ctx, e);
    }


}
