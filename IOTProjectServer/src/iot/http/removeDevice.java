package iot.http;

import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import iot.dao.DAO;
import iot.mvc.DeviceObject;

@WebServlet("/removeDevice")
public class removeDevice extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public removeDevice() {
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

		// TODO: make more robust, this is a temp solution so avoid empty inserts
		if (deviceID != null){
			DAO.removeDevice(deviceID);
		}

		
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}	
}
