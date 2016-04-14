package iot.rmi;

import java.rmi.*;

/**
 * Interface for the RMI registry
 */
public interface RMIInterface extends Remote{
	public Boolean isConnected(String deviceID) throws RemoteException;
}