package us.sosia.video.stream.channel;

import java.awt.Dimension;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;

import us.sosia.video.stream.handler.H264StreamDecoder;
import us.sosia.video.stream.handler.StreamClientHandler;
import us.sosia.video.stream.handler.StreamClientListener;
import us.sosia.video.stream.handler.StreamFrameListener;

/**
 * Настройка обработчиков клиента
 */
public class StreamClientChannelPipelineFactory implements ChannelPipelineFactory{
	protected final StreamClientListener streamClientListener;
	protected final StreamFrameListener streamFrameListener;
	protected final Dimension dimension;

	/**
	 * Конструктор
	 * @param streamClientListener обработчик статуса каналов
	 * @param streamFrameListener обработчик видео
	 * @param dimension размер видео
	 */
	public StreamClientChannelPipelineFactory(
			StreamClientListener streamClientListener,
			StreamFrameListener streamFrameListener, Dimension dimension) {
		super();
		this.streamClientListener = streamClientListener;
		this.streamFrameListener = streamFrameListener;
		this.dimension = dimension;
	}

	/**
	 * Настройка обработчиков клиента
	 * @return обработчик
	 * @throws Exception
	 */
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = Channels.pipeline();
		pipeline.addLast("stream client handler", new StreamClientHandler(streamClientListener));
		pipeline.addLast("frame decoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4,0,4));
		pipeline.addLast("stream handler", new H264StreamDecoder(streamFrameListener,dimension));
		return pipeline;
	}

	
	
}
