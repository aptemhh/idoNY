package us.sosia.video.stream.handler;

import org.jboss.netty.channel.Channel;

/**
 * Интерфейс обработчика сервера
 */
public interface StreamServerListener {
    /**
     * канал создан
     *
     * @param channel
     */
    public void onClientConnectedIn(Channel channel);

    /**
     * канал закрыт
     *
     * @param channel
     */
    public void onClientDisconnected(Channel channel);

    /**
     * в канале ошибка
     *
     * @param channel
     * @param t
     */
    public void onExcaption(Channel channel, Throwable t);
}
