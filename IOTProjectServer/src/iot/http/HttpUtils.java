package iot.http;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class HttpUtils {
	public HttpUtils(){
		super();
	}

	public void printJson(HttpServletResponse response, String output) throws IOException{
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(output);
		out.close();
	}
}