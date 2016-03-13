package iot.http;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import iot.dao.DAO;
import iot.models.DeviceModel;

@WebServlet(urlPatterns = {"/setDevice", "/registerDevice"})
public class SetDevice extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SetDevice() {
		super();
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DAO DAO = new DAO();
		// Get values from parameters
		String deviceID = request.getParameter("deviceID");
		String houseID = request.getParameter("houseID");
		String name = request.getParameter("name");
		String manufacturer = request.getParameter("manufacturer");
		String location = request.getParameter("location");
		String type = request.getParameter("type");
		boolean connected = Boolean.valueOf(request.getParameter("connected"));
		String currentState = request.getParameter("currentState");

		// TODO: make more robust, this is a temp solution to avoid empty inserts
		if (deviceID != null){
			// Create staff objects with parameters to send to the DAO
			DeviceModel device = new DeviceModel(deviceID, houseID, name, manufacturer, location, type, connected, currentState);
			DAO.registerDevice(device);
		}

		
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson = new Gson();
		DeviceModel device;
		DAO DAO = new DAO();
		
		 Map<String, String[]> allMap=request.getParameterMap();
		    for(String key:allMap.keySet()){
		        String[] strArr=(String[])allMap.get(key);
		        for(String val:strArr){
		            device = gson.fromJson(val, DeviceModel.class);
		            DAO.registerDevice(device);
		        }   
		    }		
	}	
}
