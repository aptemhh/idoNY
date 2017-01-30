package us.sosia.video.stream.handler;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IMetaData;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IPixelFormat.Type;
import com.xuggle.xuggler.IRational;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IStreamCoder.Direction;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.IConverter;

/**
 * Видео кодер
 */
public class H264StreamEncoder extends OneToOneEncoder {
	protected final static Logger logger = LoggerFactory.getLogger(Logger.class);
	protected IStreamCoder iStreamCoder;
	{
		iStreamCoder = IStreamCoder.make(Direction.ENCODING, ICodec.ID.CODEC_ID_MPEG4);
	}
	protected final IPacket iPacket = IPacket.make();
	protected long startTime;
	protected final Dimension dimension;

	/**
	 * Конструктор кодировщика видео
	 * @param dimension размер видео
	 */
	public H264StreamEncoder(Dimension dimension) {
		super();
		this.dimension = dimension;
		initialize();
	}

	/**
	 * настройка кодировщика
	 */
	private void initialize() {
		iStreamCoder.setNumPicturesInGroupOfPictures(25);
		iStreamCoder.setBitRate(200000);
		iStreamCoder.setBitRateTolerance(10000);
		iStreamCoder.setPixelType(Type.YUV420P);
		iStreamCoder.setHeight(dimension.height);
		iStreamCoder.setWidth(dimension.width);
		iStreamCoder.setFlag(IStreamCoder.Flags.FLAG_QSCALE, true);
		iStreamCoder.setGlobalQuality(0);
		IRational rate = IRational.make(25, 1);
		iStreamCoder.setFrameRate(rate);

		iStreamCoder.setTimeBase(IRational.make(rate.getDenominator(), rate.getNumerator()));
		IMetaData codecOptions = IMetaData.make();
		codecOptions.setValue("tune", "zerolatency");
		int revl = iStreamCoder.open(codecOptions, null);
		if (revl < 0) {
			throw new RuntimeException("could not open the coder");
		}
	}

	protected Object encode(ChannelHandlerContext ctx, Channel channel,
							Object msg) throws Exception {
		return encode(msg);
	}

	/**
	 * Закодировать
	 * @param msg видеобуффер
	 * @return закодированный буффер
	 * @throws Exception
	 */
	public Object encode(Object msg) throws Exception {
		logger.info("encode the frame");
		BufferedImage bufferedImage = (BufferedImage) msg;
		BufferedImage convetedImage = convertToType(bufferedImage, BufferedImage.TYPE_3BYTE_BGR);
		IConverter converter = ConverterFactory.createConverter(convetedImage, Type.YUV420P);
		long now = System.currentTimeMillis();
		if (startTime == 0) {
			startTime = now;
		}
		IVideoPicture pFrame = converter.toPicture(convetedImage, (now - startTime) * 1000);
		iStreamCoder.encodeVideo(iPacket, pFrame, 0);
		pFrame.delete();
		converter.delete();
		if (iPacket.isComplete()) {
			try {
				ByteBuffer byteBuffer = iPacket.getByteBuffer();
				if (iPacket.isKeyPacket()) {
					logger.info("key frame");
				}
				ChannelBuffer channelBuffe = ChannelBuffers.copiedBuffer(byteBuffer.order(ByteOrder.BIG_ENDIAN));
				return channelBuffe;

			} finally {
				iPacket.reset();
			}
		} else {
			return null;
		}
	}

	/**
	 * конвектор буффер изображения
	 * @param sourceImage исходное изображение
	 * @param targetType желаемый тип изображения
	 * @return желаемое изображение
	 */
	public static BufferedImage convertToType(BufferedImage sourceImage,
											  int targetType) {
		BufferedImage image;
		if (sourceImage.getType() == targetType)
			image = sourceImage;
		else {
			image = new BufferedImage(sourceImage.getWidth(),
					sourceImage.getHeight(), targetType);
			image.getGraphics().drawImage(sourceImage, 0, 0, null);
		}
		return image;
	}
}
