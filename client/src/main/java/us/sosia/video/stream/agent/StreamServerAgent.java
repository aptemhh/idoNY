package us.sosia.video.stream.agent;

import com.github.sarxos.webcam.Webcam;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.sosia.video.stream.channel.StreamServerChannelPipelineFactory;
import us.sosia.video.stream.handler.H264StreamEncoder;
import us.sosia.video.stream.handler.StreamServerListener;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;
import java.util.concurrent.*;

/**
 * Сервер видео потока
 */
public class StreamServerAgent implements IStreamServerAgent {
    protected final static Logger logger = LoggerFactory.getLogger(StreamServerAgent.class);
    protected final Webcam webcam;
    protected final Dimension dimension;
    protected final ChannelGroup channelGroup = new DefaultChannelGroup();
    protected final ServerBootstrap serverBootstrap;

    protected final H264StreamEncoder h264StreamEncoder;
    protected volatile boolean isStreaming;
    protected ScheduledExecutorService timeWorker;
    protected ExecutorService encodeWorker;
    protected int FPS = 25;
    protected ScheduledFuture<?> imageGrabTaskFuture;
    List<String> socketAddresses;

    /**
     * Конструктор сервера
     *
     * @param webcam          вебкамера
     * @param dimension       размер видео
     * @param socketAddresses одобренные адреса
     */
    public StreamServerAgent(Webcam webcam, Dimension dimension, List<String> socketAddresses) {
        super();
        this.webcam = webcam;
        this.dimension = dimension;

        this.serverBootstrap = new ServerBootstrap();
        this.serverBootstrap.setFactory(new NioServerSocketChannelFactory(
                Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool()));
        this.serverBootstrap.setPipelineFactory(new StreamServerChannelPipelineFactory(
                new StreamServerListenerIMPL(),
                dimension));
        this.timeWorker = new ScheduledThreadPoolExecutor(1);
        this.encodeWorker = Executors.newSingleThreadExecutor();
        this.h264StreamEncoder = new H264StreamEncoder(dimension);
        this.socketAddresses = socketAddresses;
    }

    /**
     * Запуск сервера
     *
     * @param streamAddress ip port сервера
     */
    public void start(SocketAddress streamAddress) {
        logger.info("Server started :{}", streamAddress);
        Channel channel = serverBootstrap.bind(streamAddress);
        channelGroup.add(channel);
    }

    /**
     * Остановить сервер
     */
    public void stop() {
        logger.info("server is stoping");
        channelGroup.close();
        timeWorker.shutdown();
        encodeWorker.shutdown();
        serverBootstrap.releaseExternalResources();
    }

    /**
     * Обработчик клиент
     */
    private class StreamServerListenerIMPL implements StreamServerListener {

        /**
         * Клиент подключился
         *
         * @param channel канал клиента
         */
        public void onClientConnectedIn(final Channel channel) {
            if (socketAddresses.stream().anyMatch(inetSocketAddress ->
                    inetSocketAddress.equals(((InetSocketAddress) channel.getRemoteAddress()).getHostName()))) {
                logger.info("Подключился :" + ((InetSocketAddress) channel.getRemoteAddress()).getHostName());
                channelGroup.add(channel);
            }

            if (!isStreaming) {
                Runnable imageGrabTask = new ImageGrabTask();
                ScheduledFuture<?> imageGrabFuture =
                        timeWorker.scheduleWithFixedDelay(imageGrabTask, 0,
                                1000 / FPS, TimeUnit.MILLISECONDS);
                imageGrabTaskFuture = imageGrabFuture;
                isStreaming = true;
            }
        }

        /**
         * Клиент отключился
         *
         * @param channel канал клиента
         */
        public void onClientDisconnected(Channel channel) {
            logger.info("Отключился :" + channel.getRemoteAddress());
            channelGroup.remove(channel);
            int size = channelGroup.size();
            if (size == 1) {
                //cancel the task
                imageGrabTaskFuture.cancel(false);
                webcam.close();
                isStreaming = false;
            }
        }

        /**
         * Обработчик ошибки в канале
         *
         * @param channel канал клиента
         * @param t       ошибка
         */
        public void onExcaption(Channel channel, Throwable t) {
            channelGroup.remove(channel);
            channel.close();
            int size = channelGroup.size();
            if (size == 1) {
                //cancel the task
                imageGrabTaskFuture.cancel(false);
                webcam.close();
                isStreaming = false;
            }
        }

        /**
         * Обработчик получения изображения с камеры
         */
        private class ImageGrabTask implements Runnable {

            /**
             * получения изображения с камеры
             */
            public void run() {
                BufferedImage bufferedImage = webcam.getImage();
                encodeWorker.execute(new EncodeTask(bufferedImage));
            }
        }

        /**
         * Шифрование изображения и отправка клиентам
         */
        private class EncodeTask implements Runnable {
            private final BufferedImage image;

            /**
             * Конструктор
             *
             * @param image Буффер изображения
             */
            public EncodeTask(BufferedImage image) {
                super();
                this.image = image;

            }

            /**
             * Упаковка и отправка
             */
            public void run() {
                try {
                    Object msg = h264StreamEncoder.encode(image);
                    if (msg != null) {
                        channelGroup.write(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
