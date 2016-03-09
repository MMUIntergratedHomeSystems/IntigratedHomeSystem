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
import com.hivemq.spi.services.ClientService;

public class MainClass extends PluginEntryPoint{
	
	Logger log = LoggerFactory.getLogger(MainClass.class);
	public final ClientService clientService;
	
	@Inject
	public MainClass(final ClientService clientService){
		log.info("RMI Server.");

		this.clientService = clientService;
	}
	
	@PostConstruct
	public void postConstruct() {
		// Add Call backs here first!!
		
		RMIImpl impl;
		try {
			// Start rmiregistery
			// need to set codebase?
			// check policy settings too
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

