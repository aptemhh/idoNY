package Micr.client;

import com.xuggle.ferry.IBuffer;
import com.xuggle.xuggler.*;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.IConverter;
import org.apache.axis.utils.ByteArray;
import org.apache.axis.utils.ByteArrayOutputStream;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import us.sosia.video.stream.handler.H264StreamDecoder;
import us.sosia.video.stream.handler.H264StreamEncoder;

import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by idony on 20.11.16.
 */
public class Micr {

    protected IStreamCoder iStreamCoder;
    {
        iStreamCoder = IStreamCoder.make(IStreamCoder.Direction.ENCODING, ICodec.ID.CODEC_ID_MPEG4);
        if(iStreamCoder==null) System.out.println("errr");
    }
    public IStreamCoder getiStreamCoder()
    {
        return iStreamCoder;
    }

    public static void main(String[] args) {

        AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, true);
        TargetDataLine microphone;
        AudioInputStream audioInputStream;
        SourceDataLine sourceDataLine;
        try {
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            microphone = (TargetDataLine) AudioSystem.getLine(info);
            microphone.open(format);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int numBytesRead;
            int CHUNK_SIZE = 1024;
            byte[] data = new byte[microphone.getBufferSize() / 5];
            System.out.println("Пошла");
            microphone.start();
            System.out.println("Отсановилась");
            int bytesRead = 0;

            long startTime = System.currentTimeMillis();
            numBytesRead = microphone.read(data, 0, CHUNK_SIZE);


            Micr micr = new Micr();
            micr.initialize();
            IStreamCoder iStreamCoder = micr.getiStreamCoder();
            IPacket iPacket = IPacket.make();

            IAudioSamples samples = IAudioSamples.make(CHUNK_SIZE, 4, IAudioSamples.Format.FMT_S16);


            samples.put(data, 0, 0, numBytesRead);

            long now = System.currentTimeMillis();
            if (startTime == 0) {
                startTime = now;
            }
            iStreamCoder.encodeAudio(iPacket, samples, (now - startTime) * 1000);


            ChannelBuffer channelBuffers= ChannelBuffers.copiedBuffer(iPacket.getByteBuffer().order(ByteOrder.BIG_ENDIAN));


            byte audioData[] = out.toByteArray();

            System.out.println("Воспроизвожу");
            // Get an input stream on the byte array
            // containing the data
            InputStream byteArrayInputStream = new ByteArrayInputStream(
                    audioData);
            audioInputStream = new AudioInputStream(byteArrayInputStream, format, audioData.length / format.getFrameSize());
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            sourceDataLine.open(format);
            sourceDataLine.start();
            int cnt = 0;
            byte tempBuffer[] = new byte[10000];
            try {
                while ((cnt = audioInputStream.read(tempBuffer, 0, tempBuffer.length)) != -1) {
                    if (cnt > 0) {
                        // Write data to the internal buffer of
                        // the data line where it will be
                        // delivered to the speaker.
                        sourceDataLine.write(tempBuffer, 0, cnt);
                    }// end if
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Block and wait for internal buffer of the
            // data line to empty.
            sourceDataLine.drain();
            sourceDataLine.close();
            microphone.close();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    private void initialize(){
        //setup
        iStreamCoder.setNumPicturesInGroupOfPictures(25);

        iStreamCoder.setBitRate(200000);
        iStreamCoder.setBitRateTolerance(10000);
        iStreamCoder.setPixelType(IPixelFormat.Type.YUV420P);
        iStreamCoder.setDefaultAudioFrameSize(50);
        iStreamCoder.setFlag(IStreamCoder.Flags.FLAG_QSCALE, true);
        iStreamCoder.setGlobalQuality(0);
        //rate
        IRational rate = IRational.make(25, 1);
        iStreamCoder.setFrameRate(rate);
        //time base
        //iStreamCoder.setAutomaticallyStampPacketsForStream(true);
        iStreamCoder.setTimeBase(IRational.make(rate.getDenominator(),rate.getNumerator()));
        IMetaData codecOptions = IMetaData.make();
        codecOptions.setValue("tune", "zerolatency");// equals -tune zerolatency in ffmpeg
        //open it
        int revl = iStreamCoder.open(codecOptions, null);
        if (revl < 0) {
            throw new RuntimeException("could not open the coder");
        }
    }
}
