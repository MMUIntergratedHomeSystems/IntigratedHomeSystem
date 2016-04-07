package iot.mqtt;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;


/**
 * Used to start the MqttServerRecive client on start up.
 *
 */
@SuppressWarnings("serial")
public class Initialize extends HttpServlet
{

	// Start up the mqtt receiver
	public void init() throws ServletException
	{
		try {
			MqttServerReceive server = new MqttServerReceive();
			System.out.println("----------");
			System.out.println("---------- Initialized successfully ----------");
			System.out.println("----------");
		} catch (Exception e) {
			System.out.println("----------");
			System.out.println("---------- Failed ----------");
			System.out.println("----------");
			e.printStackTrace();
		}			
	}
}