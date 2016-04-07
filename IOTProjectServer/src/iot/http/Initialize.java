package iot.http;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;

import iot.mqtt.MqttServerReceive;


@SuppressWarnings("serial")
public class Initialize extends HttpServlet
{

	// Start up the mqtt receiver
	public void init() throws ServletException
	{
		MqttServerReceive server = new MqttServerReceive();
		System.out.println("----------");
		System.out.println("---------- Initialize successfully ----------");
		System.out.println("----------");
	}
}