package Micr.server;


import javax.sound.sampled.*;

import com.xuggle.xuggler.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.sosia.video.stream.handler.frame.FrameEncoder;

import com.xuggle.ferry.IBuffer;

/**
 * Created by User on 008 08.01.17.
 */
public class sdgsdg  {
    protected final static Logger logger = LoggerFactory.getLogger(Logger.class);
    ICodec.ID idCodec =ICodec.ID.CODEC_ID_AAC;

    protected IStreamCoder iAudioStreamCoder;
    protected final IPacket iPacket = IPacket.make();

    protected final FrameEncoder frameEncoder;

    public sdgsdg( boolean usingInternalFrameEncoder) {
        super();


        iAudioStreamCoder= IStreamCoder.make(IStreamCoder.Direction.ENCODING, idCodec);
        if (usingInternalFrameEncoder) {
            frameEncoder = new FrameEncoder(4);
        }else {
            frameEncoder = null;
        }
        try {
            openJavaSound();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        initialize();
    }

    private void initialize(){
        iAudioStreamCoder.setChannels(2);
        iAudioStreamCoder.setFrameRate(IRational.make(44100,4));
        iAudioStreamCoder.setBitRate(16);

    }

    private static SourceDataLine mLine;
    private static boolean isFirst = true;
    private static void openJavaSound() throws LineUnavailableException
    {
        AudioFormat audioFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                44100.0F, 16, 2, 4, 44000, false);

        AudioFormat format = new AudioFormat(44000,
                16,
                2,
                true, /* xuggler defaults to signed 16 bit samples */
                false);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        mLine = (SourceDataLine) AudioSystem.getLine(info);
        /**
         * if that succeeded, try opening the line.
         */
        mLine.open(audioFormat);
        /**
         * And if that succeed, start the line.
         */
        mLine.start();


    }

    private static synchronized void playJavaSound(IAudioSamples aSamples)
    {
        if (isFirst) {
            isFirst = false;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /**
         * We're just going to dump all the samples into the line.
         */
        byte[] rawBytes = aSamples.getData().getByteArray(0, aSamples.getSize());
        mLine.write(rawBytes, 0, rawBytes.length);
    }

    public Object encode(byte[] audioBuf, int numBytesRead, long sTime, AudioFormat audioFormat, TargetDataLine aline) throws Exception {

        IBuffer iBuf = IBuffer.make(null, audioBuf, 0, numBytesRead);
        IAudioSamples smp = IAudioSamples.make(iBuf,audioFormat.getChannels(), IAudioSamples.Format.FMT_S16);

        if (smp != null) {
            long numSample = numBytesRead / smp.getSampleSize();
            smp.setComplete(true, numSample, (int) audioFormat.getSampleRate(), audioFormat.getChannels(), IAudioSamples.Format.FMT_S16, 400);
            smp.put(audioBuf, 1, 0, aline.available());

        }
        playJavaSound(smp);
        iPacket.setStreamIndex(0);
        iAudioStreamCoder.encodeAudio(iPacket, smp, 0);
        System.out.println("packet size: " + iPacket.getSize());










        return null;
//
//        if (iPacket.isComplete()) {
//
//            try{
//                ByteBuffer byteBuffer = iPacket.getByteBuffer();
//                if (iPacket.isKeyPacket()) {
//                    logger.info("key frame");
//                }
//                ChannelBuffer channelBuffe = ChannelBuffers.copiedBuffer(byteBuffer.order(ByteOrder.BIG_ENDIAN));
//                if (frameEncoder != null) {
//                    System.out.println("using frame encoder");
//                    return frameEncoder.encode(channelBuffe);
//                }
//                return channelBuffe;
//
//            }finally{
//                iPacket.reset();
//            }
//        }else{
//            return encode(data,numBytesRead);
//        }
    }

}