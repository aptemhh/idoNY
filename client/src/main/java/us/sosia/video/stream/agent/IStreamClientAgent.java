package us.sosia.video.stream.agent;

import java.net.SocketAddress;

public interface IStreamClientAgent {
	/**
	 * Покдлючиться к серверу
	 * @param streamServerAddress адрес сервера
	 */
	public void connect(SocketAddress streamServerAddress);

	/**
	 * Остановить сервер
	 */
	public void stop();
}
