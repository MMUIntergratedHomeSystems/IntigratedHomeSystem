package iot.http;

import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.List;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import iot.dao.DAO;
import iot.mvc.StateObject;

@WebServlet("/setState")
public class setTestState extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public setTestState() {
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
		String state = request.getParameter("state");

		// TODO: make more robust, this is a temp solution so avoid empty inserts
		if (deviceID != null){
			// Create staff objects with parameters to send to the DAO
			StateObject stateObj = new StateObject(deviceID, state, new Date());
			DAO.updateState(stateObj);
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
