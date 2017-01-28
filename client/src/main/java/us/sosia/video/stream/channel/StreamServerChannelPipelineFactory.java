package us.sosia.video.stream.channel;

import java.awt.Dimension;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.frame.LengthFieldPrepender;

import us.sosia.video.stream.handler.StreamServerHandler;
import us.sosia.video.stream.handler.StreamServerListener;

public class StreamServerChannelPipelineFactory implements ChannelPipelineFactory{
	protected final StreamServerListener streamServerListener;
	protected final Dimension dimension;

	public StreamServerChannelPipelineFactory(
			StreamServerListener streamServerListener, Dimension dimension) {
		super();
		this.streamServerListener = streamServerListener;
		this.dimension = dimension;
	}



	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = Channels.pipeline();
		pipeline.addLast("frame encoder", new LengthFieldPrepender(4,false));
		pipeline.addLast("stream server handler", new StreamServerHandler(streamServerListener));
		return pipeline;
	}

	
}
