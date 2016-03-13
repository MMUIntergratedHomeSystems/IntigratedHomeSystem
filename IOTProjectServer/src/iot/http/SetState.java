package iot.http;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import iot.models.ResponseModel;
import iot.models.StateModel;
import iot.mqtt.MqttServer;

@WebServlet("/setState")
public class SetState extends HttpServlet {
	private static final long serialVersionUID = 1L;
	HttpUtils utils = new HttpUtils();
	Gson gson = new Gson();
	ResponseModel responceObj = new ResponseModel(false, "Unknown Error");

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SetState() {
		super();
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Get values from parameters
		String deviceID = request.getParameter("deviceID");
		String state = request.getParameter("state");

		// Create staff objects with parameters to send to the DAO
		StateModel stateObj = new StateModel(deviceID, state, null);
		sendToMqtt(stateObj);
		
		// Print output
		String output = gson.toJson(responceObj);
		utils.printJson(response, output);
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StateModel stateObj;

		Map<String, String[]> allMap=request.getParameterMap();
		for(String key:allMap.keySet()){
			String[] strArr=(String[])allMap.get(key);
			for(String val:strArr){
				stateObj = gson.fromJson(val, StateModel.class);
				sendToMqtt(stateObj);
			}
		}
		// Print output
		String output = gson.toJson(responceObj);
		utils.printJson(response, output);
	}

	public void sendToMqtt(StateModel state){
		MqttServer mqttServer = new MqttServer();
		
		if (state.getDeviceID() != null){
			responceObj = mqttServer.send(state.getDeviceID(), state.getState());
		} else {
			responceObj.setSucsess(false);
			responceObj.setMessage("No device details sent");
		}
	}
}
