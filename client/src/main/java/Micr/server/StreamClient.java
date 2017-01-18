package Micr.server;

/**
 * Created by User on 008 08.01.17.
 */
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.net.InetSocketAddress;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xuggle.xuggler.IAudioSamples;


import us.sosia.video.stream.agent.StreamClientAgent;
import us.sosia.video.stream.handler.StreamFrameListener;

public class StreamClient {
    /**
     * @author kerr
     * */
    private static SourceDataLine mLine;
    private static boolean isFirst = true;


    protected final static Logger logger = LoggerFactory.getLogger(StreamClient.class);
    public static void main(String[] args) {
        //setup the videoWindow


        //setup the connection
        StreamClientAgent clientAgent = new StreamClientAgent(new StreamFrameListenerIMPL(),null);
        clientAgent.connect(new InetSocketAddress("localhost", 20000));
        try {
            openJavaSound();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }


    protected static class StreamFrameListenerIMPL implements StreamFrameListener{
        private volatile long count = 0;

        public void onFrameReceived(BufferedImage image) {
            logger.info("frame received :{}",count++);

        }

        public void onAudioRecieved(IAudioSamples samples) {
            playJavaSound(samples);
        }
    }

    private static void openJavaSound() throws LineUnavailableException
    {
        AudioFormat audioFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                44100.0F, 16, 2, 4, 44100, false);

        AudioFormat format = new AudioFormat(44100,
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
}