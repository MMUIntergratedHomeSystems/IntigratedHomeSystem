package iot.rmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient {
	public boolean isDeviceConncted(String deviceID) throws RemoteException, NotBoundException{
		RMIInterface rmi = connectToRMI();
		return rmi.isConnected(deviceID);	
	}

	public RMIInterface connectToRMI() throws RemoteException, NotBoundException{
		if (System.getSecurityManager() == null) {
			System.setProperty("java.rmi.server.codebase", "file:/opt/hivemq/plugins/IOTProjectHivePlugin-0.0.1-SNAPSHOT.jar");
			System.setSecurityManager(new SecurityManager());
		}

		Registry registry = LocateRegistry.getRegistry("127.0.0.1",1099);		
		RMIInterface rmiInterface = (RMIInterface) registry.lookup("HivePlugin");

		return rmiInterface;		
	}
}