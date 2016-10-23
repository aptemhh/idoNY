package us.sosia.video.stream.agent;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.net.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.sosia.video.stream.agent.ui.SingleVideoDisplayWindow;
import us.sosia.video.stream.handler.StreamFrameListener;

public class StreamClient {
	/**
	 * @author kerr
	 * */
	private final static Dimension dimension = new Dimension(320,240);
	private final static SingleVideoDisplayWindow displayWindow = new SingleVideoDisplayWindow("Stream example",dimension);
	protected final static Logger logger = LoggerFactory.getLogger(StreamClient.class);
	public static void main(String[] args) {
		//setup the videoWindow
		displayWindow.setVisible(true);
		
		//setup the connection
		logger.info("setup dimension :{}",dimension);
		StreamClientAgent clientAgent = new StreamClientAgent(new StreamFrameListenerIMPL(),dimension);
		try {
			clientAgent.connect(new InetSocketAddress(NetworkInterface.getNetworkInterfaces().nextElement().getInetAddresses().nextElement().getCanonicalHostName(), 20000));
		} catch (SocketException e) {
			e.printStackTrace();
		}


	}
	
	
	protected static class StreamFrameListenerIMPL implements StreamFrameListener{
		private volatile long count = 0;
		public void onFrameReceived(BufferedImage image) {
			displayWindow.updateImage(image);
		}
		
	}
	

}
