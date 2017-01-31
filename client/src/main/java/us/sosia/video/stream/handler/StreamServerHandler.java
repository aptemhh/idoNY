package us.sosia.video.stream.handler;

import org.jboss.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Обработчик прокси для логивания
 */
public class StreamServerHandler extends SimpleChannelHandler {
    protected final StreamServerListener streamServerListener;
    protected final static Logger logger = LoggerFactory.getLogger(StreamServerHandler.class);

    /**
     * Конструктор
     *
     * @param streamServerListener обработчик
     */
    public StreamServerHandler(StreamServerListener streamServerListener) {
        super();
        this.streamServerListener = streamServerListener;
    }

    /**
     * в канале ошибка
     *
     * @param ctx канал
     * @param e   ошибка
     * @throws Exception
     */
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
            throws Exception {
        Channel channel = e.getChannel();
        Throwable t = e.getCause();
        logger.debug("exception caught at :{},exception :{}", channel, t);
        streamServerListener.onExcaption(channel, t);
    }

    /**
     * в канале ошибка
     *
     * @param ctx канал
     * @param e   статус
     * @throws Exception
     */
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
            throws Exception {
        Channel channel = e.getChannel();
        logger.info("channel connected :{}", channel);
        streamServerListener.onClientConnectedIn(channel);
        super.channelConnected(ctx, e);
    }

    /**
     * канал закрыт
     *
     * @param ctx канал
     * @param e   статус
     * @throws Exception
     */
    public void channelDisconnected(ChannelHandlerContext ctx,
                                    ChannelStateEvent e) throws Exception {
        Channel channel = e.getChannel();
        logger.info("channel disconnected :{}", channel);
        streamServerListener.onClientDisconnected(channel);
        super.channelDisconnected(ctx, e);
    }

    public void writeComplete(ChannelHandlerContext ctx, WriteCompletionEvent e)
            throws Exception {
        super.writeComplete(ctx, e);
    }

}
