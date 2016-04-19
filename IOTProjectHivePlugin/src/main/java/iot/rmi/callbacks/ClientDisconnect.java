package iot.rmi.callbacks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hivemq.spi.callback.events.OnDisconnectCallback;
import com.hivemq.spi.security.ClientData;

public class ClientDisconnect implements OnDisconnectCallback {
	Logger log = LoggerFactory.getLogger(ClientDisconnect.class);

	/**
	 * This method is called from the HiveMQ on a client disconnect.
	 *
	 * @param arg0       Useful information about the clients authentication state and credentials.
	 * @param arg1 When true the connection of the client broke down without a
	 *                         {@link com.hivemq.spi.message.DISCONNECT} message and if false then the client
	 *                         disconnected properly with a {@link com.hivemq.spi.message.DISCONNECT} message.
	 */

	@Override
	public void onDisconnect(ClientData arg0, boolean arg1) {
		log.info("Client {} disconnected", arg0.getClientId());
		
		SendToServer server = new SendToServer();
		server.updateDB(arg0.getClientId(), false);
	}
}