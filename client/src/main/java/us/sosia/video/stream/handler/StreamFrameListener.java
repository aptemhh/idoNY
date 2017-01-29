package us.sosia.video.stream.handler;

import com.xuggle.xuggler.IAudioSamples;

import java.awt.image.BufferedImage;

public interface StreamFrameListener {
	void onFrameReceived(BufferedImage image);
}
