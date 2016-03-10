package iot.rmi.callbacks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hivemq.spi.callback.CallbackPriority;
import com.hivemq.spi.callback.events.OnConnectCallback;
import com.hivemq.spi.callback.exception.RefusedConnectionException;
import com.hivemq.spi.message.CONNECT;
import com.hivemq.spi.security.ClientData;

public class ClientConnect implements OnConnectCallback{

	 Logger log = LoggerFactory.getLogger(ClientConnect.class);
	
	    /**
	     * The priority is used when more than one OnConnectCallback is implemented to determine the order.
	     * If there is only one callback, which implements a certain interface, the priority has no effect.
	     *
	     * @return callback priority
	     */
	    @Override
	    public int priority() {
	        return CallbackPriority.MEDIUM;
	    }

	    /**
	     * This is the callback method, which is called by the HiveMQ core, if a client has sent,
	     * a {@link CONNECT} Message and was successfully authenticated. In this acme there is only
	     * a logging statement, normally the behavior would be implemented in here.
	     *
	     * @param connect    The {@link CONNECT} message from the client.
	     * @param clientData Useful information about the clients authentication state and credentials.
	     * @throws RefusedConnectionException This exception should be thrown, if the client is
	     *                                    not allowed to connect.
	     */
		@Override
		public void onConnect(CONNECT arg0, ClientData arg1) throws RefusedConnectionException {
			log.info("Client {} connected", arg1.getClientId());
			SendToServer server = new SendToServer();
			server.updateDB(arg1.getClientId(), true);			
		}
}