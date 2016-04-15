package iot.http;

import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import iot.dao.DAO;
import iot.dao.DAOInterface;
import iot.models.ResponseModel;

@WebServlet("/removeDevice")
public class RemoveDevice extends HttpServlet {
	private static final long serialVersionUID = 1L;
	HttpUtils utils = new HttpUtils();
	Gson gson = new Gson();
	DAOInterface DAO = new DAO();
	ResponseModel responceObj = new ResponseModel(false, "Unknown Error");
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
		
		// Get values from parameters
		String deviceID = request.getParameter("deviceID");

		// TODO: make more robust, this is a temp solution so avoid empty inserts
		if (deviceID != null){
			responceObj = DAO.removeDevice(deviceID);
		}

		// Print output
		String output = gson.toJson(responceObj);
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
