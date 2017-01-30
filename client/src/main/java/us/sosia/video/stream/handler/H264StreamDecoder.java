package us.sosia.video.stream.handler;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ch.qos.logback.classic.Logger;
import com.xuggle.xuggler.*;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;
import org.slf4j.LoggerFactory;


import com.xuggle.ferry.IBuffer;
import com.xuggle.xuggler.IStreamCoder.Direction;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.ConverterFactory.Type;
import com.xuggle.xuggler.video.IConverter;


/**
 * видео декодер
 **/
public class H264StreamDecoder extends OneToOneDecoder {

	protected final static org.slf4j.Logger logger = LoggerFactory.getLogger(H264StreamDecoder.class);
	protected final IStreamCoder iStreamCoder = IStreamCoder.make(Direction.DECODING, ICodec.ID.CODEC_ID_MPEG4);
	protected final Type type = ConverterFactory.findRegisteredConverter(ConverterFactory.XUGGLER_BGR_24);
	protected final StreamFrameListener streamFrameListener;
	protected final Dimension dimension;

	/**
	 * Конструктор декодера
	 *
	 * @param streamFrameListener
	 * @param dimension            размер

	 */
	public H264StreamDecoder(StreamFrameListener streamFrameListener,
							 Dimension dimension) {
		super();
		this.streamFrameListener = streamFrameListener;
		this.dimension = dimension;
		iStreamCoder.open(null, null);
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, final Object msg) throws Exception {

		if (msg == null) {
			throw new NullPointerException("you cannot pass into an null to the decode");
		}
		ChannelBuffer frameBuffer = (ChannelBuffer) msg;

		int size = frameBuffer.readableBytes();
		logger.info("decode the frame size :{}", size);
		IBuffer iBuffer = IBuffer.make(null, size);
		IPacket iPacket = IPacket.make(iBuffer);
		iPacket.getByteBuffer().put(frameBuffer.toByteBuffer());
		if (!iPacket.isComplete()) {
			return null;
		}
		IVideoPicture picture = IVideoPicture.make(IPixelFormat.Type.YUV420P,
				dimension.width, dimension.height);
		try {
			int postion = 0;
			int packageSize = iPacket.getSize();
			while (postion < packageSize) {
				postion += iStreamCoder.decodeVideo(picture, iPacket, postion);
				if (postion < 0) {
					throw new RuntimeException("error "
							+ " decoding video");
				}
				if (picture.isComplete()) {
					IConverter converter = ConverterFactory.createConverter(type
							.getDescriptor(), picture);
					BufferedImage image = converter.toImage(picture);
					if (streamFrameListener != null) {
						streamFrameListener.onFrameReceived(image);
					}
					converter.delete();
				} else {
					picture.delete();
					iPacket.delete();
					return null;
				}
				picture.getByteBuffer().clear();
			}
		} finally {
			if (picture != null) {
				picture.delete();
			}
			iPacket.delete();
		}
		return null;
	}
}
