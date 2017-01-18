package Micr.server;

import com.xuggle.ferry.IBuffer;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IStreamCoder;
import org.apache.axis.utils.ByteArrayOutputStream;
import us.sosia.video.stream.server.Person;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;

/**
 * Created by idony on 20.11.16.
 */
public class Micr {

    public static void main(String[] args) {
        sdgsdg sdgsdg = new sdgsdg(false);
//        TargetDataLine line;
//        AudioFormat format = new AudioFormat(44100.0F, 16, 2, true, false);
//
//        line = null;
//        DataLine.Info info = new DataLine.Info(TargetDataLine.class,
//
//                format); // format is an AudioFormat object
//        if (!AudioSystem.isLineSupported(info)) {
//            System.out.println("ЗВУК НЕ ПОДДЕРЖИВАЕТСЯ!!!");
//        }
//        // Obtain and open the line.
//        try {
//
//            line = (TargetDataLine) AudioSystem.getLine(info);
//            line.open(format);
//        } catch (LineUnavailableException ex) {
//            // Handle the error ...
//        }
//        byte[] data = new byte[4096];
//        int numBytesRead;
//
//        for (; ; ) {
//            numBytesRead = line.read(data, 0, 4096);
//            try {
//                sdgsdg.encode(data, numBytesRead);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }


//       for (ICodec i:ICodec.getInstalledCodecs() ){
////        if(IStreamCoder.make(IStreamCoder.Direction.ENCODING, i)!=null )
//        if(i.getType()== ICodec.Type.CODEC_TYPE_AUDIO)
//        {
//            if(i.canEncode())
//            {
//                for(IAudioSamples.Format format1:i.getSupportedAudioSampleFormats())
//                {
//                    System.out.println(i.getName()+"  format: "+format1.name()+"  cpt:"+i.getMyCPtr()
//                            +"  ChannelLayouts:"+i.getSupportedAudioChannelLayouts()+"  SampleRates:"+ i.getSupportedAudioSampleRates());
//                }
//            }
//        }
//    }
        final int audioStreamIndex = 1;
        final int audioStreamId = 1;
        final int channelCount = 2;
        int sampleRate;

        AudioFormat audioFormat;
        AudioInputStream audioInputStream;
        TargetDataLine aline=null;
        AudioFormat targetType;

        byte[] audioBuf;
        int audionumber;

        long sTime;



        audioFormat = new AudioFormat(44100.0F, 16, channelCount, true, false);
        sampleRate = (int) audioFormat.getSampleRate();
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
        try {
            aline = (TargetDataLine) AudioSystem.getLine(info);
            aline.open(audioFormat);
            aline.start();

        }
        catch (LineUnavailableException e)
        {
        }
        int bufferSize = (int) audioFormat.getSampleRate() * audioFormat.getFrameSize();
        audioBuf = new byte[bufferSize];





        for(int i=aline.available();i!=88200;i=aline.available())
        {
            try {
                Thread.sleep(200);

                System.out.println(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        for (;;)
        if (aline.available() == 88200) {

            int nBytesRead = aline.read(audioBuf, 0, aline.available());//audioBuf.length);//aline.available());
            if (nBytesRead > 0) {sTime=System.nanoTime();
                try {
                    sdgsdg.encode(  audioBuf,nBytesRead,sTime,audioFormat,aline);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }



}
