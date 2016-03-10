package iot.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hivemq.spi.PluginEntryPoint;
import com.hivemq.spi.callback.registry.CallbackRegistry;
import com.hivemq.spi.services.ClientService;

import iot.rmi.callbacks.*;


public class MainClass extends PluginEntryPoint{
	
	Logger log = LoggerFactory.getLogger(MainClass.class);
	public final ClientService clientService;
    private final ClientConnect clientConnect;
    private final ClientDisconnect clientDisconnect;
	
	@Inject
	public MainClass(final ClientService clientService, final ClientConnect clientConnect, final ClientDisconnect clientDisconnect){
		log.info("Intelligent Home Plugin.");
		this.clientService = clientService;
		this.clientConnect = clientConnect;
		this.clientDisconnect = clientDisconnect;
		// TODO: Add dependencies
	}
	
	@PostConstruct
	public void postConstruct() {
		// TODO: Add Call backs here first!!
		CallbackRegistry callbackRegistry = getCallbackRegistry();
        callbackRegistry.addCallback(clientConnect);
        callbackRegistry.addCallback(clientDisconnect);
		
		
		// Start RMI server
		startRMI();
	}
	
	public void startRMI(){
		RMIImpl impl;
		try {
			// TODO: Start rmiregistery
			// TODO: need to set codebase?
			// TODO: check policy settings too
			//Registry registry =LocateRegistry.createRegistry(1099);
			impl = new RMIImpl("somthing", clientService);
			RMIInterface stub = (RMIInterface) UnicastRemoteObject.exportObject(impl, 0);
			Registry registry = LocateRegistry.getRegistry();
			registry.rebind("HivePlugin", stub);			
			
			//registry = LocateRegistry.getRegistry(1099);
			//registry.rebind("Hello", new RMIImpl("Hello, world!"));
			log.info("RMI Server is ready.");
		} catch (RemoteException e) {
			log.info("Error: "+e);
		}
	}
}


/*
if (System.getSecurityManager() == null) {
	log.info("RMI Server null.");
	System.setSecurityManager(new RMISecurityManager());
	log.info("RMI Server created.");
}		*/

