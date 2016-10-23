package us.sosia.video.stream.handler.frame;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferFactory;
import org.jboss.netty.buffer.ChannelBuffers;

public class DirectChannelBufferFactory implements ChannelBufferFactory{
	

	public ChannelBuffer getBuffer(int capacity) {
 		return ChannelBuffers.directBuffer(capacity);
	}

	public ChannelBuffer getBuffer(ByteOrder endianness, int capacity) {
 		return ChannelBuffers.directBuffer(endianness, capacity);
	}


	public ChannelBuffer getBuffer(byte[] array, int offset, int length) {
		ChannelBuffer channelBuffer = ChannelBuffers.directBuffer(length);
		channelBuffer.writeBytes(array, offset, length);
		return channelBuffer;
	}


	public ChannelBuffer getBuffer(ByteOrder endianness, byte[] array,
			int offset, int length) {
		ChannelBuffer channelBuffer = ChannelBuffers.directBuffer(endianness,length);
		channelBuffer.writeBytes(array, offset, length);
 		return channelBuffer;
	}

	public ChannelBuffer getBuffer(ByteBuffer nioBuffer) {
		int size = nioBuffer.capacity();
		ChannelBuffer channelBuffer = ChannelBuffers.directBuffer(nioBuffer.order(), size);
		channelBuffer.writeBytes(nioBuffer);
		return channelBuffer;
	}

	public ByteOrder getDefaultOrder() {
 		return ByteOrder.BIG_ENDIAN;
	}

}
