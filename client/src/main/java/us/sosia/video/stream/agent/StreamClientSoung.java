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

public class StreamClientSoung implements IStreamClientAgent {
    protected final static Logger logger = LoggerFactory.getLogger(StreamClientSoung.class);
    protected final ClientBootstrap clientBootstrap;
    protected Channel clientChannel;
    private volatile Boolean run = true;

    public StreamClientSoung() {
        super();
        this.clientBootstrap = new ClientBootstrap();
        this.clientBootstrap.setFactory(new NioClientSocketChannelFactory(
                Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool()));

        this.clientBootstrap.setPipelineFactory(
                new ChannelPipelineFactory() {
                    public ChannelPipeline getPipeline() throws Exception {
                        ChannelPipeline pipeline = Channels.pipeline();
                        pipeline.addLast("co", new StreamClientHandler(new StreamClientListenerIMPL()));

                        pipeline.addLast("q3e", new OneToOneDecoder() {
                            @Override
                            protected Object decode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
                                final byte[] bytes = new byte[((BigEndianHeapChannelBuffer) msg).writerIndex()];
                                ((BigEndianHeapChannelBuffer) msg).readBytes(bytes);

                                synchronized (run) {
                                    if (run)
                                        playJavaSound(bytes);
                                }


                                ((BigEndianHeapChannelBuffer) msg).resetWriterIndex();
                                return null;
                            }
                        });

                        return pipeline;
                    }
                });
        try {
            openJavaSound();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }


    public void connect(SocketAddress streamServerAddress) {
        logger.info("going to connect to stream server :{}", streamServerAddress);
        clientBootstrap.connect(streamServerAddress);
    }


    public void stop() {
        clientChannel.close();
        clientBootstrap.releaseExternalResources();
    }

    private static SourceDataLine mLine;

    private static void openJavaSound() throws LineUnavailableException {

        AudioFormat format = new AudioFormat(16000.0f, 16, 1, true, true);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        mLine = (SourceDataLine) AudioSystem.getLine(info);
        /**
         * if that succeeded, try opening the line.
         */
        mLine.open(format);
        /**
         * And if that succeed, start the line.
         */
        mLine.start();


    }

    private static synchronized void playJavaSound(byte[] bytes) {
        mLine.write(bytes, 0, bytes.length);

    }

    protected class StreamClientListenerIMPL implements StreamClientListener {


        public void onConnected(Channel channel) {
            logger.info("stream connected to server at :{}", channel);
            clientChannel = channel;
        }


        public void onDisconnected(Channel channel) {
            logger.info("stream disconnected to server at :{}", channel);
        }


        public void onException(Channel channel, Throwable t) {
            logger.debug("exception at :{},exception :{}", channel, t);
        }

    }

    public void setAudio(Boolean audio) {
        synchronized (run) {
            run = audio;
        }
    }

    public Boolean getAudio() {
        return run;
    }
}
