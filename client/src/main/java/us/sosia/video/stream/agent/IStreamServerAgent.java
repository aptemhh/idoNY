package us.sosia.video.stream.agent;

import java.net.SocketAddress;

public interface IStreamServerAgent {
	/**
	 * Запуск сервера
	 * @param streamAddress адрес сервера
	 */
	public void start(SocketAddress streamAddress);

	/**
	 * Остановить сервер
	 */
	public void stop();
}
