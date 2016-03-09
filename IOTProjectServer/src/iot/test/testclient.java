package iot.test;

/***************************************************************
 *     Client program for the "Hello, world!" RMI example.
 *****************************************************************/

import java.rmi.Naming;

public class testclient
{
	public static void main (String[] argv) {
		try {
			if (System.getSecurityManager() == null) {
				System.setSecurityManager(new SecurityManager());
			}

			RMIInterface hello =             // your PC address here 52.88.194.67
					// 127.0.0.1
					(RMIInterface) Naming.lookup ("rmi://52.88.194.67/Hello"); 
			System.out.println (hello);//.isConnected("test"));
		}
		catch (Exception e){
			System.out.println ("HelloClient exception: " + e);}
	}
}
