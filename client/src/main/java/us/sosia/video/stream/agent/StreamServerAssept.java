package us.sosia.video.stream.agent;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.BigEndianHeapChannelBuffer;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.LengthFieldPrepender;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.sosia.video.stream.channel.StreamServerChannelPipelineFactory;
import us.sosia.video.stream.handler.H264StreamDecoder;
import us.sosia.video.stream.handler.StreamClientHandler;
import us.sosia.video.stream.handler.StreamServerHandler;
import us.sosia.video.stream.handler.StreamServerListener;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.CharBuffer;
import java.util.concurrent.Executors;

/**
 * Created by idony on 25.12.16.
 */
public class StreamServerAssept implements IStreamServerAgent {
    protected final static Logger logger = LoggerFactory.getLogger(StreamServerAssept.class);
    protected final ServerBootstrap serverBootstrap;
    protected final ChannelGroup channelGroup = new DefaultChannelGroup();
    public StreamServerAssept(final OneToOneDecoder oneToOneDecoder) {
        super();

        this.serverBootstrap = new ServerBootstrap();
        this.serverBootstrap.setFactory(new NioServerSocketChannelFactory(
                Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool()));
        this.serverBootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();
                pipeline.addLast("Пришел клиент", new StreamServerHandler(new StreamServerListener() {
                    public void onClientConnectedIn(Channel channel) {
                        channelGroup.add(channel);
                    }

                    public void onClientDisconnected(Channel channel) {
                        channelGroup.remove(channel);
                    }

                    public void onExcaption(Channel channel, Throwable t) {
                        channelGroup.remove(channel);
                        channel.close();
                    }
                }));
                pipeline.addLast("frame encoder", new LengthFieldPrepender(4,false));
                pipeline.addLast("reader",oneToOneDecoder);
                return pipeline;
            }
        });
    }
    public void start(SocketAddress streamAddress) {
        logger.info("Server started :{}",streamAddress);
        Channel channel = serverBootstrap.bind(streamAddress);
        channelGroup.add(channel);
    }

    public void stop() {
        logger.info("server is stoping");
        channelGroup.close();
        serverBootstrap.releaseExternalResources();
    }
}
