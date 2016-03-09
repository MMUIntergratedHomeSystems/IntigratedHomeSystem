package iot.rmi;

import java.rmi.*;

import com.hivemq.spi.services.ClientService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// extends UnicastRemoteObject 
public class RMIImpl implements RMIInterface {

	Logger log = LoggerFactory.getLogger(RMIImpl.class);
	private String message;
	public ClientService clientService;

	public RMIImpl(String msg, ClientService clientService) throws RemoteException {
		super();
		this.clientService = clientService;
		message = msg;
		log.info("Plugin class");
	}

	@Override
	public Boolean isConnected(String deviceID) throws RemoteException {
		log.info("Plugin class - isConnected() deviceID:"+deviceID);
		return clientService.isClientConnectedLocal(deviceID);
	}

	@Override
	public String say() throws RemoteException {
		return message;
	}
}
