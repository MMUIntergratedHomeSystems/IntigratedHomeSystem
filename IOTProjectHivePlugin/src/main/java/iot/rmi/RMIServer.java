//package iot.rmi;
//
//
//import java.io.File;
//import java.rmi.Naming;
//import java.rmi.RMISecurityManager;
//import java.rmi.RemoteException;
//import java.rmi.registry.LocateRegistry;
//import java.rmi.registry.Registry;
//import java.rmi.server.UnicastRemoteObject;
//
//import javax.inject.Inject;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.hivemq.spi.services.ClientService;
//
//public class RMIServer {
//	public RMIServer(){
//		super();
//	}
//	//private final ClientService clientService;
//
//	//@Inject	
//	//	public PluginServer() throws RemoteException {
//	//		//final ClientService clientService
//	//		super();
//	//		Logger log = LoggerFactory.getLogger(PluginServer.class);
//	//		log.info("Plugin contruct");
//	//		//this.clientService = clientService;
//	//		main(null);
//	//	}
//
//	//public static boolean isRegistered = false; 
//	//public static RMIInterface service; 
//	//public RMIServer(){
//		public static void main (String[] argv)	{
//		Logger log = LoggerFactory.getLogger(RMIServer.class);
//		log.info("RMI Server.");
//
//		/*System.setProperty("java.security.policy","file:./security.policy");
//
//		if (System.getSecurityManager() == null) {
//			log.info("RMI Server null.");
//			System.setSecurityManager(new SecurityManager());
//			log.info("RMI Server created.");
//		}*/
//
//		//if(!isRegistered){ 
//		/*try { 
//			log.info("1");
//			service = new RMIImpl(); 
//			log.info("2");
//			Registry registry = LocateRegistry.createRegistry(1099);
//			log.info("3");
//			registry = LocateRegistry.getRegistry(1099);
//			log.info("4");
//			registry.rebind("Hello", service);
//			log.info("5");
//
//			//Interface stub = 
//			//		(Interface) UnicastRemoteObject.exportObject(service, 1099); 
//
//
//			log.info("Remote service bound"); */
//			//	isRegistered = true; 
//		
//	/*	try{
//		Naming.rebind("Hello", new RMIImpl("Hello, world!"));
//		log.info("Remote service bound");
//		} catch (Exception e) { 
//			log.info("Remote service exception: "+e); 
//		} 
//		log.info("RMI Server is ready.");*/
//		//} 
//	}
//}
//
////	try {
//
///*File f = new File("/opt/hivemq/plugins/AllSecurity.policy");
//			log.info("f.exists(): "+f.exists());
//
//			f = new File("/opt/hivemq/plugins/IOTProjectHivePlugin-0.0.1-SNAPSHOT.jar");
//			log.info("f.exists(): "+f.exists());
// */
//
////System.setProperty("java.security.policy","file:///opt/hivemq/plugins/AllSecurity.policy");
////System.setProperty("java.rmi.server.codebase","file:///opt/hivemq/plugins/-");
////System.setProperty("java.rmi.server.hostname","127.0.0.1:1099");//52.88.194.67");
////IOTProjectHivePlugin-0.0.1-SNAPSHOT.jar
///*		if (System.getSecurityManager() == null) {
//				log.info("RMI Server null.");
//				System.setSecurityManager(new RMISecurityManager());
//				log.info("RMI Server created.");
//			}*/
//
///*Plugin obj  = new Plugin();
//			Interface stub = (Interface) UnicastRemoteObject.exportObject( obj , 0 ) ;
//
//			  LocateRegistry.getRegistry().bind( "Hello" , stub ) ;*/
//
//
////	Naming.rebind ("Hello", new Plugin()); //clientService
//
///*}  
//		catch (Exception e) {
//			log.info("RMI Server failed: " + e);
//		}*/
