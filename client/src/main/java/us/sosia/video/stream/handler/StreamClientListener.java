package us.sosia.video.stream.handler;

import org.jboss.netty.channel.Channel;

/**
 * Интерфейс обработчика клиента
 */
public interface StreamClientListener {
    /**
     * канал создан
     *
     * @param channel
     */
    public void onConnected(Channel channel);

    /**
     * канал закрыт
     *
     * @param channel
     */
    public void onDisconnected(Channel channel);

    /**
     * в канале ошибка
     *
     * @param channel
     * @param t
     */
    public void onException(Channel channel, Throwable t);
}
