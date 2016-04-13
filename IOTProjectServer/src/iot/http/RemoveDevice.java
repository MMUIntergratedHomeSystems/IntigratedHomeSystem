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
import iot.dao.DAOInterface;

@WebServlet("/removeDevice")
public class RemoveDevice extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RemoveDevice() {
		super();
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DAOInterface DAO = new DAO();
		
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
