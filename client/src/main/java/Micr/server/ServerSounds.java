//package Micr.server;
//
//import com.github.sarxos.webcam.Webcam;
//import com.xuggle.xuggler.IAudioSamples;
//import org.apache.axis.utils.ByteArrayOutputStream;
//import org.jboss.netty.bootstrap.ServerBootstrap;
//import org.jboss.netty.channel.Channel;
//import org.jboss.netty.channel.ChannelPipeline;
//import org.jboss.netty.channel.ChannelPipelineFactory;
//import org.jboss.netty.channel.Channels;
//import org.jboss.netty.channel.group.ChannelGroup;
//import org.jboss.netty.channel.group.DefaultChannelGroup;
//import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
//import org.jboss.netty.handler.codec.frame.LengthFieldPrepender;
//import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import us.sosia.video.stream.agent.IStreamServerAgent;
//import us.sosia.video.stream.agent.StreamServerAgent;
//import us.sosia.video.stream.agent.StreamServerAssept;
//import us.sosia.video.stream.channel.StreamServerChannelPipelineFactory;
//import us.sosia.video.stream.handler.H264StreamEncoder;
//import us.sosia.video.stream.handler.StreamServerListener;
//
//import javax.sound.sampled.TargetDataLine;
//import java.awt.*;
//import java.awt.List;
//import java.awt.image.BufferedImage;
//import java.net.InetSocketAddress;
//import java.net.SocketAddress;
//import java.util.*;
//import java.util.concurrent.*;
//import java.util.function.Predicate;
//
///**
// * Created by User on 007 07.01.17.
// */
//public class ServerSounds implements IStreamServerAgent{
//    protected final static Logger logger = LoggerFactory.getLogger(StreamServerAgent.class);
//    protected final TargetDataLine microphone;
//    protected final Dimension dimension;
//    protected final ChannelGroup channelGroup = new DefaultChannelGroup();
//    protected final ServerBootstrap serverBootstrap;
//
//    protected final H264StreamEncoder h264StreamEncoder;
//    protected volatile boolean isStreaming;
//    protected ScheduledExecutorService timeWorker;
//    protected ExecutorService encodeWorker;
//    protected int FPS = 25;
//    protected ScheduledFuture<?> imageGrabTaskFuture;
//    java.util.List<String> socketAddresses;
//    public ServerSounds(TargetDataLine microphone, Dimension dimension, java.util.List<String> socketAddresses) {
//        super();
//        this.microphone = microphone;
//        this.dimension = dimension;
//
//        this.serverBootstrap = new ServerBootstrap();
//        this.serverBootstrap.setFactory(new NioServerSocketChannelFactory(
//                Executors.newCachedThreadPool(),
//                Executors.newCachedThreadPool()));
//        this.serverBootstrap.setPipelineFactory(new StreamServerChannelPipelineFactory(
//                new ServerSounds.StreamServerListenerIMPL(),
//                dimension));
//        this.timeWorker = new ScheduledThreadPoolExecutor(1);
//        this.encodeWorker = Executors.newSingleThreadExecutor();
//        this.h264StreamEncoder = new H264StreamEncoder(dimension, false);
//        this.socketAddresses = socketAddresses;
//    }
//
//
//
//    public int getFPS() {
//        return FPS;
//    }
//
//    public void setFPS(int fPS) {
//        FPS = fPS;
//    }
//
//
//    public void start(SocketAddress streamAddress) {
//        logger.info("Server started :{}",streamAddress);
//        Channel channel = serverBootstrap.bind(streamAddress);
//        channelGroup.add(channel);
//    }
//
//
//    public void stop() {
//        logger.info("server is stoping");
//        channelGroup.close();
//        timeWorker.shutdown();
//        encodeWorker.shutdown();
//        serverBootstrap.releaseExternalResources();
//    }
//
//
//    private class StreamServerListenerIMPL implements StreamServerListener {
//
//
//        public void onClientConnectedIn(final Channel channel) {
////            if (socketAddresses.stream().anyMatch(new Predicate<String>() {
////                public boolean test(String inetSocketAddress) {
////                    return inetSocketAddress.equals(((InetSocketAddress)channel.getRemoteAddress()).getHostName());
////                }
////            }))
////            {
////                logger.info("Подключился :"+((InetSocketAddress)channel.getRemoteAddress()).getHostName());
////                channelGroup.add(channel);
////            }
//
//            if (!isStreaming) {
//                //do some thing
//                Runnable imageGrabTask = new ServerSounds.StreamServerListenerIMPL.ImageGrabTask();
//                ScheduledFuture<?> imageGrabFuture =
//                        timeWorker.scheduleWithFixedDelay(imageGrabTask,
//                                0,
//                                1000 / FPS,
//                                TimeUnit.MILLISECONDS);
//                imageGrabTaskFuture = imageGrabFuture;
//                isStreaming = true;
//            }
//        }
//
//
//        public void onClientDisconnected(Channel channel) {
//            logger.info("Отключился :"+channel.getRemoteAddress());
//            channelGroup.remove(channel);
//            int size = channelGroup.size();
//            if (size == 1) {
//                //cancel the task
//                imageGrabTaskFuture.cancel(false);
//                microphone.close();
//                isStreaming = false;
//            }
//        }
//
//
//        public void onExcaption(Channel channel, Throwable t) {
//            channelGroup.remove(channel);
//            channel.close();
//            int size = channelGroup.size();
//            if (size == 1) {
//                //cancel the task
//                imageGrabTaskFuture.cancel(false);
//                microphone.close();
//                isStreaming = false;
//
//            }
//
//        }
//
//
//        final int channelCount = 1;
//        IAudioSamples smp;
//
//        private class ImageGrabTask implements Runnable{
//
//
//            public void run() {
//                ByteArrayOutputStream bufferedImage = microphone.getImage();
//                /**
//                 * using this when the h264 encoder is added to the pipeline
//                 * */
//                //channelGroup.write(bufferedImage);
//                /**
//                 * using this when the h264 encoder is inside this class
//                 * */
//                encodeWorker.execute(new ServerSounds.StreamServerListenerIMPL.EncodeTask(bufferedImage));
//            }
//
//        }
//
//        private class EncodeTask implements Runnable{
//            private final ByteArrayOutputStream image;
//            public EncodeTask(ByteArrayOutputStream image) {
//                super();
//                this.image = image;
//
//            }
//
//
//            public void run() {
//                try {
//                    Object msg = h264StreamEncoder.encode(image);
//                    if (msg != null) {
//                        channelGroup.write(msg);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }
//
//
//    }
//
//
//
//
//
//}
