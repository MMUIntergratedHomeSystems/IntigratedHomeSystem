package iot.rmi;

import java.rmi.*;

import com.hivemq.spi.services.ClientService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *Interface for the RMI implementation
 */
public class RMIImpl implements RMIInterface {

	// Start logger
	Logger log = LoggerFactory.getLogger(RMIImpl.class);
	public ClientService clientService;

	public RMIImpl(String msg, ClientService clientService) throws RemoteException {
		super();
		this.clientService = clientService;
		log.info("Plugin class");
	}

	/** 
	 * Returns if a device is currently connected to the HiveMq server
	 */
	@Override
	public Boolean isConnected(String deviceID) throws RemoteException {
		log.info("Plugin class - isConnected() deviceID:"+deviceID);
		return clientService.isClientConnectedLocal(deviceID);
	}
}
