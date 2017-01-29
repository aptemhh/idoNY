package us.sosia.video.stream.agent;

import java.awt.Dimension;
import java.net.SocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.sosia.video.stream.channel.StreamClientChannelPipelineFactory;
import us.sosia.video.stream.handler.StreamClientListener;
import us.sosia.video.stream.handler.StreamFrameListener;

/**
 * Клиент видео
 */
public class StreamClientAgent implements IStreamClientAgent{
	protected final static Logger logger = LoggerFactory.getLogger(StreamClientAgent.class);
	protected final ClientBootstrap clientBootstrap;
	protected final StreamClientListener streamClientListener;
	protected final StreamFrameListener streamFrameListener;
	protected final Dimension dimension;
	protected Channel clientChannel;

	/**
	 * Конструктор
	 * @param streamFrameListener обработчик видео
	 * @param dimension размер видео
	 */
	public StreamClientAgent(StreamFrameListener streamFrameListener,
			Dimension dimension) {
		super();
		this.dimension = dimension;
		this.clientBootstrap = new ClientBootstrap();
		this.clientBootstrap.setFactory(new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool()));
		this.streamFrameListener = streamFrameListener;
		this.streamClientListener = new StreamClientListenerIMPL();
		this.clientBootstrap.setPipelineFactory(
				new StreamClientChannelPipelineFactory(
						streamClientListener,
						streamFrameListener, 
						dimension));
	}

	/**
	 * Подключиться к видео серверу
	 * @param streamServerAddress
	 */
	public void connect(SocketAddress streamServerAddress) {
		logger.info("going to connect to stream server :{}",streamServerAddress);
		clientBootstrap.connect(streamServerAddress);
	}

	/**
	 * Остановить подключение к серверу
	 */
	public void stop() {
		clientChannel.close();
		clientBootstrap.releaseExternalResources();
	}

	/**
	 * Обработчик подключения к серверу
	 */
	protected class StreamClientListenerIMPL implements StreamClientListener{

		/**
		 * создано подключение с сервером
		 * @param channel канал с севером
		 */
		public void onConnected(Channel channel) {
			logger.info("stream connected to server at :{}",channel);
			clientChannel = channel;
		}

		/**
		 * Обработчик отключения потока
		 * @param channel канал с севером
		 */
		public void onDisconnected(Channel channel) {
			logger.info("stream disconnected to server at :{}",channel);
		}

		/**
		 * Обработчик ошибки в канале
		 * @param channel канал с севером
		 * @param t ошибка
		 */
		public void onException(Channel channel, Throwable t) {
			logger.debug("exception at :{},exception :{}",channel,t);
		}
		
	}
	
}
