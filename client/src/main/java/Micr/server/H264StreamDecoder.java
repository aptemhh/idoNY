package Micr.server;

/**
 * Created by User on 008 08.01.17.
 */
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.sosia.video.stream.handler.StreamFrameListener;
import us.sosia.video.stream.handler.frame.FrameDecoder;

import com.xuggle.ferry.IBuffer;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IStreamCoder.Direction;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.ConverterFactory.Type;
import com.xuggle.xuggler.video.IConverter;
/**
 * This codec will encode the bufferedImage to h264 stream
 * **/
public class H264StreamDecoder extends OneToOneDecoder {
    protected final static Logger logger = LoggerFactory.getLogger(H264StreamDecoder.class);
    protected final IStreamCoder iAudioStreamCoder = IStreamCoder.make(Direction.DECODING, ICodec.ID.CODEC_ID_AAC);
    protected final Type type = ConverterFactory.findRegisteredConverter(ConverterFactory.XUGGLER_BGR_24);
    protected final StreamFrameListener streamFrameListener;


    protected final FrameDecoder frameDecoder;
    protected final ExecutorService decodeWorker;

    /**
     * Cause there may be one or more image in the frame,so we need an Stream listener here to get all the image
     */
    public H264StreamDecoder(StreamFrameListener streamFrameListener, boolean internalFrameDecoder, boolean decodeInOtherThread) {
        super();
        this.streamFrameListener = streamFrameListener;
        if (internalFrameDecoder) {
            frameDecoder = new FrameDecoder(4);
        } else {
            frameDecoder = null;
        }
        if (decodeInOtherThread) {
            decodeWorker = Executors.newSingleThreadExecutor();
        } else {
            decodeWorker = null;
        }

        initialize();
    }

    private void initialize() {
        iAudioStreamCoder.open(null, null);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel,
                            final Object msg) throws Exception {
        if (msg == null) {
            throw new NullPointerException("you cannot pass into an null to the decode");
        }
        ChannelBuffer frameBuffer;
        if (frameDecoder != null) {
            frameBuffer = frameDecoder.decode((ChannelBuffer) msg);
            if (frameBuffer == null) {
                return null;
            }

        } else {
            frameBuffer = (ChannelBuffer) msg;
        }

        int size = frameBuffer.readableBytes();
        logger.info("decode the frame size :{}", size);

        //start to decode
        IBuffer iBuffer = IBuffer.make(null, size);
        IPacket iPacket = IPacket.make(iBuffer);
        iPacket.getByteBuffer().put(frameBuffer.toByteBuffer());
        //decode the packet
        if (!iPacket.isComplete()) {
            return null;
        }
        logger.info("packet stream index: " + iPacket.getFlags());

        IAudioSamples samples = IAudioSamples.make(1024, 1);

        int offset = 0;

        while (offset < iPacket.getSize()) {
            int bytesDecoded = iAudioStreamCoder.decodeAudio(samples, iPacket, offset);
            if (bytesDecoded < 0)
                throw new RuntimeException("got error decoding audio in stream");

            offset += bytesDecoded;

            if (samples.isComplete()) {
                if (streamFrameListener != null) {
                    streamFrameListener.onAudioRecieved(samples);
                }
            }
        }

        return null;

    }
}