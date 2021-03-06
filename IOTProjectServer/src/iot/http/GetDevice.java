package iot.http;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import iot.dao.*;
import iot.models.DeviceModel;

@WebServlet("/getDevice")
public class GetDevice extends HttpServlet {
	private static final long serialVersionUID = 1L;
	HttpUtils utils = new HttpUtils();
	DAOInterface DAO = new DAO();
	DeviceModel device;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetDevice() {
		super();
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String deviceID = request.getParameter("deviceID");
		String output = null;
		
		if (deviceID != null){
			device = DAO.getDeviceInfo(deviceID);
			output = gson.toJson(device);
		} else {
			List<DeviceModel> device = DAO.getAllDeviceInfo();
			output = gson.toJson(device);
		}
		
		// Print output
		utils.printJson(response, output);
		
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}	
}
