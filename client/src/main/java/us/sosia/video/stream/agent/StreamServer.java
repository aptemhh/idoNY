package us.sosia.video.stream.agent;

import java.awt.Dimension;
import java.io.ByteArrayOutputStream;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;

import com.github.sarxos.webcam.Webcam;

import javax.sound.sampled.*;


public class StreamServer {

	/**
	 * @param args
	 * @author kerr
	 */
	public static void main(String[] args) {
		Webcam.setAutoOpenMode(true);
		Webcam webcam = Webcam.getDefault();
		Dimension dimension = new Dimension(320, 240);
		webcam.setViewSize(dimension);
		if(webcam==null)
			System.out.println("err");
		StreamServerAgent serverAgent = new StreamServerAgent(webcam, dimension);
		try {
			serverAgent.start(new InetSocketAddress(NetworkInterface.getNetworkInterfaces().nextElement().getInetAddresses().nextElement().getCanonicalHostName(),20000));
		} catch (SocketException e) {
			e.printStackTrace();
		}


		try {
			System.out.println(NetworkInterface.getNetworkInterfaces().nextElement().getInetAddresses().nextElement().getCanonicalHostName());
		} catch (SocketException e) {
			e.printStackTrace();
		}


	}
}


