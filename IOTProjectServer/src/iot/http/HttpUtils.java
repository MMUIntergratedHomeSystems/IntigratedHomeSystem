package iot.http;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class HttpUtils {
	public HttpUtils(){
		super();
	}

	/**
	 * Takes HTTP response and a JSON String that will be printed out for clients interfacing with the system.
	 *  
	 * @param response
	 * @param output
	 * @throws IOException
	 */
	public void printJson(HttpServletResponse response, String output) throws IOException{
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(output);
		out.close();
	}
}