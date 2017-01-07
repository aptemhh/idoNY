package us.sosia.video.stream.agent;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import com.xuggle.ferry.IBuffer;
import com.xuggle.xuggler.IAudioSamples;
import org.apache.axis.utils.ByteArrayOutputStream;
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

import com.github.sarxos.webcam.Webcam;

import javax.sound.sampled.*;

public class StreamServerAgent implements IStreamServerAgent{
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
		this.h264StreamEncoder = new H264StreamEncoder(dimension, false);
		this.socketAddresses = socketAddresses;
	}	
	
	
	
	public int getFPS() {
		return FPS;
	}

	public void setFPS(int fPS) {
		FPS = fPS;
	}


	public void start(SocketAddress streamAddress) {
		logger.info("Server started :{}",streamAddress);
		Channel channel = serverBootstrap.bind(streamAddress);
		channelGroup.add(channel);
	}
	

	public void stop() {
		logger.info("server is stoping");
		channelGroup.close();
		timeWorker.shutdown();
		encodeWorker.shutdown();
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
				//do some thing
				Runnable imageGrabTask = new ImageGrabTask();
				ScheduledFuture<?> imageGrabFuture =
						timeWorker.scheduleWithFixedDelay(imageGrabTask,
								0,
								1000 / FPS,
								TimeUnit.MILLISECONDS);
				imageGrabTaskFuture = imageGrabFuture;
				isStreaming = true;
			}
		}


		public void onClientDisconnected(Channel channel) {
			logger.info("Отключился :"+channel.getRemoteAddress());
			channelGroup.remove(channel);
			int size = channelGroup.size();
			if (size == 1) {
				//cancel the task
				imageGrabTaskFuture.cancel(false);
				webcam.close();
				isStreaming = false;
			}
		}


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


		final int channelCount = 1;
		IAudioSamples smp;

	private class ImageGrabTask implements Runnable{


		public void run() {
			BufferedImage bufferedImage = webcam.getImage();
			/**
			 * using this when the h264 encoder is added to the pipeline
			 * */
			//channelGroup.write(bufferedImage);
			/**
			 * using this when the h264 encoder is inside this class
			 * */
			encodeWorker.execute(new EncodeTask(bufferedImage));
		}
		
	}
	
	private class EncodeTask implements Runnable{
		private final BufferedImage image;
		public EncodeTask(BufferedImage image) {
			super();
			this.image = image;

		}


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
