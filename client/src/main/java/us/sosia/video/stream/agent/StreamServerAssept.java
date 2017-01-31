package us.sosia.video.stream.agent;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.LengthFieldPrepender;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.concurrent.Executors;

/**
 * Created by idony on 25.12.16.
 * Класс сервера для принятия ключей и создания ободренного списка ip адресов
 */
public class StreamServerAssept implements IStreamServerAgent {
    protected final static Logger logger = LoggerFactory.getLogger(StreamServerAssept.class);
    protected final ServerBootstrap serverBootstrap;
    private static StreamServerAssept streamServerAssept;
    protected final ChannelGroup channelGroup = new DefaultChannelGroup();

    /**
     * Контруктор-одиночка
     *
     * @param oneToOneDecoder оброботчик
     * @return одиночку
     */
    public static StreamServerAssept getStreamServerAssept(final OneToOneDecoder oneToOneDecoder) {
        if (streamServerAssept == null)
            streamServerAssept = new StreamServerAssept(oneToOneDecoder);
        return streamServerAssept;
    }

    /**
     * Геттер класса
     *
     * @return одиночку
     */
    public static StreamServerAssept getStreamServerAssept() {
        return streamServerAssept;
    }

    /**
     * Конструктор
     *
     * @param oneToOneDecoder обработчик входных сообщений
     */
    private StreamServerAssept(final OneToOneDecoder oneToOneDecoder) {
        super();
        serverBootstrap = new ServerBootstrap();
        serverBootstrap.setFactory(new NioServerSocketChannelFactory(
                Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool()));
        serverBootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();
                pipeline.addLast("frame encoder", new LengthFieldPrepender(4, false));
                pipeline.addLast("reader", oneToOneDecoder);
                return pipeline;
            }
        });
    }

    Channel channel = null;

    /**
     * Запуск сервера
     *
     * @param streamAddress
     */
    public void start(SocketAddress streamAddress) {
        logger.info("Server started :{}", streamAddress);
        channel = serverBootstrap.bind(streamAddress);
        channelGroup.add(channel);

    }

    /**
     * Остановить сервер
     */
    public void stop() {
        logger.info("server is stoping");

        channelGroup.close();
        serverBootstrap.releaseExternalResources();
    }
}
