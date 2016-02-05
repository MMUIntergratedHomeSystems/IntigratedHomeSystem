package iot.http;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import iot.dao.DAO;
import iot.mvc.DeviceObject;

@WebServlet("/getDevice")
public class getDevice extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getDevice() {
		super();
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		DAO DAO = new DAO();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String deviceID = request.getParameter("deviceID");
		String output = null;
		
		if (deviceID != null){
			DeviceObject device = DAO.getDeviceInfo(deviceID);
			output = gson.toJson(device);
		} else {
			List<DeviceObject> device = DAO.getAllDeviceInfo();
			output = gson.toJson(device);
		}
		
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("application/json");
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
