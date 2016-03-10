package iot.http;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import iot.mqtt.MqttServer;
import iot.mqtt.ResponseModel;

@WebServlet("/testHive")
public class TestHive extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TestHive() {
		super();
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*RMIClient client = new RMIClient();
		String output = null;
		try {
			output = String.valueOf(client.isDeviceConncted("test"));
		} catch (NotBoundException e) {
			output += e;
		}*/
		
		ResponseModel resp;
		String output = null;
		MqttServer server = new MqttServer();
		
		resp = server.send("light123","on");
		output += resp.toString();
		resp = server.send("egg","on");
		output += resp.toString();
		resp = server.send("asd","on");
		output += resp.toString();
		
		PrintWriter out = response.getWriter();
		out.print(output);
		out.close();
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}	
}
