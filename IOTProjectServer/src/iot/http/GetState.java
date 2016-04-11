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

import iot.dao.DAO;
import iot.models.StateModel;

@WebServlet("/getState")
public class GetState extends HttpServlet {
	private static final long serialVersionUID = 1L;
	HttpUtils utils = new HttpUtils();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetState() {
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
		String log = request.getParameter("log");
		String output = null;
		
		if (deviceID != null){
			if (log != null){
				List<StateModel> state = DAO.getStateInfo(deviceID);
				output = gson.toJson(state);
			} else {
				StateModel state = DAO.getLastStateInfo(deviceID);
				output = gson.toJson(state);
			}
		} else {
			if (log != null){
				List<StateModel> state = DAO.getAllStateInfo();
				output = gson.toJson(state);
			} else {
				// TODO: need to return only last known for each
				List<StateModel> state = DAO.getAllLastStateInfo();
				output = gson.toJson(state);
			}
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
