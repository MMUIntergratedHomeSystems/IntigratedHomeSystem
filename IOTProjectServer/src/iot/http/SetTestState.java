package iot.http;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import iot.dao.DAO;
import iot.mvc.StateObject;

@WebServlet("/setState")
public class SetTestState extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SetTestState() {
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
		//doGet(request, response);
		Gson gson = new Gson();
		StateObject stateObj;
		DAO DAO = new DAO();
		
		 Map<String, String[]> allMap=request.getParameterMap();
		    for(String key:allMap.keySet()){
		        String[] strArr=(String[])allMap.get(key);
		        for(String val:strArr){
		            //System.out.println(val);
		        	stateObj = gson.fromJson(val, StateObject.class);
		        	stateObj.setDateStored(new Date());
		            //System.out.println(device);
		        	DAO.updateState(stateObj);
		        }   
		    }	
	}	
}
