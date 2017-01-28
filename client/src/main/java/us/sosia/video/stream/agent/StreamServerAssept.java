package us.sosia.video.stream.agent;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.BigEndianHeapChannelBuffer;
import org.jboss.netty.buffer.ByteBufferBackedChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.LengthFieldPrepender;
import org.jboss.netty.handler.codec.http.websocket.WebSocketFrameDecoder;
import org.jboss.netty.handler.codec.http.websocket.WebSocketFrameEncoder;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufDecoder;
import org.jboss.netty.handler.codec.replay.VoidEnum;
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
    private static StreamServerAssept streamServerAssept;
    protected final ChannelGroup channelGroup = new DefaultChannelGroup();

    public static StreamServerAssept getStreamServerAssept(final OneToOneDecoder oneToOneDecoder) {
        if(streamServerAssept==null)
            streamServerAssept=new StreamServerAssept(oneToOneDecoder);
        return streamServerAssept;
    }
    public static StreamServerAssept getStreamServerAssept() {
        return streamServerAssept;
    }


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

    public void start(SocketAddress streamAddress) {
        logger.info("Server started :{}", streamAddress);
        channel = serverBootstrap.bind(streamAddress);
        channelGroup.add(channel);

    }

    public void stop() {
        logger.info("server is stoping");

        channelGroup.close();
        serverBootstrap.releaseExternalResources();
    }
}
