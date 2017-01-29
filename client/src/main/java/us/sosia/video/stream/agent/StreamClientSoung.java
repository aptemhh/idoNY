package us.sosia.video.stream.agent;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.BigEndianHeapChannelBuffer;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.sosia.video.stream.handler.StreamClientHandler;
import us.sosia.video.stream.handler.StreamClientListener;

import javax.sound.sampled.*;
import java.net.SocketAddress;
import java.util.concurrent.Executors;

/**
 * Клиент звука
 */
public class StreamClientSoung implements IStreamClientAgent {
    protected final static Logger logger = LoggerFactory.getLogger(StreamClientSoung.class);
    protected final ClientBootstrap clientBootstrap;
    protected Channel clientChannel;
    private volatile Boolean run = true;

    /**
     * Конструктор
     */
    public StreamClientSoung() {
        super();
        this.clientBootstrap = new ClientBootstrap();
        this.clientBootstrap.setFactory(new NioClientSocketChannelFactory(
                Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool()));
        this.clientBootstrap.setPipelineFactory(
                () -> {
                    ChannelPipeline pipeline = Channels.pipeline();
                    pipeline.addLast("connector", new StreamClientHandler(new StreamClientListenerIMPL()));

                    pipeline.addLast("reader", new OneToOneDecoder() {
                        @Override
                        protected Object decode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
                            final byte[] bytes = new byte[((BigEndianHeapChannelBuffer) msg).writerIndex()];
                            ((BigEndianHeapChannelBuffer) msg).readBytes(bytes);

                                if (run)
                                    new Thread(() -> playJavaSound(bytes)).start();

                            ((BigEndianHeapChannelBuffer) msg).resetWriterIndex();
                            return null;
                        }
                    });

                    return pipeline;
                });
        try {
            openJavaSound();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * Подключение к транслятору звука
     * @param streamServerAddress адрес транслятора
     */
    public void connect(SocketAddress streamServerAddress) {
        logger.info("going to connect to stream server :{}", streamServerAddress);
        clientBootstrap.connect(streamServerAddress);
    }

    /**
     * Отсновить клиент
     */
    public void stop() {
        clientChannel.close();
        clientBootstrap.releaseExternalResources();
    }

    private static SourceDataLine mLine;

    /**
     *  Настрока аудиовыхода
     * @throws LineUnavailableException
     */
    private static void openJavaSound() throws LineUnavailableException {

        AudioFormat format = new AudioFormat(44100.0f, 16, 1, true, true);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        mLine = (SourceDataLine) AudioSystem.getLine(info);
        mLine.open(format);
        mLine.start();
    }

    /**
     * Проигрывание звука
     * @param bytes
     */
    private static synchronized void playJavaSound(byte[] bytes) {
        mLine.write(bytes, 0, bytes.length);
    }

    /**
     * Обработчик подключения к серверу
     */
    protected class StreamClientListenerIMPL implements StreamClientListener {
        /**
         * Клиент подключился к серверу
         * @param channel канал с сервером
         */
        public void onConnected(Channel channel) {
            logger.info("stream connected to server at :{}", channel);
            clientChannel = channel;
        }

        /**
         * связь разорвана
         * @param channel канал с сервером
         */
        public void onDisconnected(Channel channel) {
            logger.info("stream disconnected to server at :{}", channel);
        }

        /**
         * обработчик ошибки в канале
         * @param channel канал с сервером
         * @param t ошибка
         */
        public void onException(Channel channel, Throwable t) {
            logger.debug("exception at :{},exception :{}", channel, t);
        }

    }

    /**
     * вкл\выкл звук
     * @param audio вкл\выкл
     */
    public void setAudio(Boolean audio) {
        synchronized (run) {
            run = audio;
        }
    }

    /**
     * Включен ли звук
     * @return статус переключателя звука
     */
    public Boolean getAudio() {
        return run;
    }
}
