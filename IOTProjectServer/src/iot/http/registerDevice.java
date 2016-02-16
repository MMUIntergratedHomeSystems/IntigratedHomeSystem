package iot.http;

import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.List;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import iot.dao.DAO;
import iot.mvc.DeviceObject;

@WebServlet("/registerDevice")
public class registerDevice extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public registerDevice() {
		super();
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		response.setStatus(HttpServletResponse.SC_OK);
//		response.setContentType("text/html");
//		PrintWriter out = response.getWriter();
//		out.print("Here is some test data");
//		out.close();
		DAO DAO = new DAO();
		
		// Get values from parameters
		String deviceID = request.getParameter("deviceID");
		String name = request.getParameter("name");
		String manufacturer = request.getParameter("manufacturer");
		String location = request.getParameter("location");
		String type = request.getParameter("type");

		// TODO: make more robust, this is a temp solution to avoid empty inserts
		if (deviceID != null){
			// Create staff objects with parameters to send to the DAO
			DeviceObject device = new DeviceObject(deviceID, name, manufacturer, location, type);
			DAO.registerDevice(device);
		}

		
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson = new Gson();
		DeviceObject device;
		DAO DAO = new DAO();
		
		 Map<String, String[]> allMap=request.getParameterMap();
		    for(String key:allMap.keySet()){
		        String[] strArr=(String[])allMap.get(key);
		        for(String val:strArr){
		            //System.out.println(val);
		            device = gson.fromJson(val, DeviceObject.class);
		            //System.out.println(device);
		            DAO.registerDevice(device);
		        }   
		    }		
		
		//doGet(request, response);
	}	
}
