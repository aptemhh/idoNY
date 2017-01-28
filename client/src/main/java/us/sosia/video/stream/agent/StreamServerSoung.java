package us.sosia.video.stream.agent;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.function.Predicate;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.LengthFieldPrepender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.sosia.video.stream.handler.StreamServerHandler;
import us.sosia.video.stream.handler.StreamServerListener;

import javax.sound.sampled.*;

public class StreamServerSoung extends Thread {
    protected final static Logger logger = LoggerFactory.getLogger(StreamServerSoung.class);

    protected final ChannelGroup channelGroup = new DefaultChannelGroup();
    protected final ServerBootstrap serverBootstrap;

    protected volatile boolean isStreaming;

    List<String> socketAddresses;


    public StreamServerSoung(List<String> socketAddresses) {
        super();


        this.serverBootstrap = new ServerBootstrap();
        this.serverBootstrap.setFactory(new NioServerSocketChannelFactory(
                Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool()));
        this.serverBootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();
                pipeline.addLast("heand",new StreamServerHandler(new StreamServerListenerIMPL()));
                pipeline.addLast("frame encoder", new LengthFieldPrepender(4,false));
                return pipeline;
            }
        });
        this.socketAddresses = socketAddresses;

    }




    public void start(InetSocketAddress ip) {
        logger.info("Server-audio started :{}",ip);
        Channel channel = serverBootstrap.bind( ip);
        channelGroup.add(channel);
    }


    public void stopServer() {
        logger.info("server is stoping");
        channelGroup.close();
        serverBootstrap.releaseExternalResources();
    }


    private class StreamServerListenerIMPL implements StreamServerListener{


        public void onClientConnectedIn(final Channel channel) {
            if (socketAddresses.stream().anyMatch(new Predicate<String>() {
                public boolean test(String inetSocketAddress) {
                    return inetSocketAddress.equals(((InetSocketAddress)channel.getRemoteAddress()).getHostName());
                }
            }))
            {
                logger.info("Подключился :"+((InetSocketAddress)channel.getRemoteAddress()).getHostName());
                channelGroup.add(channel);
            }

            if (!isStreaming) {
                isStreaming = true;
            }
        }


        public void onClientDisconnected(Channel channel) {
            logger.info("Отключился :"+channel.getRemoteAddress());
            channelGroup.remove(channel);
            int size = channelGroup.size();
            if (size == 1) {
                isStreaming = false;
            }
        }


        public void onExcaption(Channel channel, Throwable t) {
            channelGroup.remove(channel);
            channel.close();
            int size = channelGroup.size();
            if (size == 1) {
                isStreaming = false;

            }

        }
    }

    @Override
    public void run() {
        try {
            AudioFormat format = new AudioFormat(16000.0f, 16, 1, true, true);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            TargetDataLine microphone = (TargetDataLine) AudioSystem.getLine(info);
            try {
                microphone.open(format);
                microphone.start();
                for(;;)
                {
                    byte[] buf = new byte[100];
                    for(int i=0;i<40;i++)
                    {
                        microphone.read(buf, 0,100);
                        ByteBuffer byteBuffer=ByteBuffer.wrap(buf).order(ByteOrder.BIG_ENDIAN);
                        channelGroup.write(ChannelBuffers.copiedBuffer(byteBuffer));
                    }
                }

            } finally {
                microphone.close();
            }
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

    }

}
