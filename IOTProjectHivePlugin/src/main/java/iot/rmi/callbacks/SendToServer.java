package iot.rmi.callbacks;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

/**
 * Sends information from the HiveMq to the MongoDB
 */
public class SendToServer {
	String baseUrl = "http://localhost:8080/IOTProjectServer";
	Logger log = LoggerFactory.getLogger(SendToServer.class);
	
	public SendToServer(){
		super();
	}
	
	public void updateDB(String deviceID, boolean state){
		String getDeviceUrl = baseUrl+"/getDevice?deviceID="+deviceID;
		String setDeviceUrl = baseUrl+"/setDevice";
		URL request;
		
		try {
			request = new URL(getDeviceUrl);
			HttpURLConnection con = (HttpURLConnection) request.openConnection();

			int responseCode = con.getResponseCode();
			log.info("\nSending 'GET' request to URL : " + getDeviceUrl);
			log.info("Response Code : " + responseCode);

			// Search for device on the DB
			BufferedReader in = new BufferedReader(
					new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			// create object from JSON response
			Gson gson = new Gson();
			DeviceObject device;
			device = gson.fromJson(response.toString(), DeviceObject.class);
			// Check the device is registered
			if (device != null){
				device.setConnected(state);
				// Update connection status
				request = new URL(setDeviceUrl);
				con = (HttpURLConnection) request.openConnection();
				
				// Send post request
				con.setRequestMethod("POST");
				con.setDoOutput(true);
				DataOutputStream wr = new DataOutputStream(con.getOutputStream());
				wr.writeBytes("PostData=" + gson.toJson(device));
				wr.flush();
				wr.close();
			    
			    responseCode = con.getResponseCode();
				log.info("\nSending 'POST' request to URL : " + setDeviceUrl);
				log.info("Response Code : " + responseCode);
				con.disconnect();
			}
			// else device is not registered so no need to update object
			
			// close connection
			con.disconnect();
			
		} catch (IOException e1) {
			log.info("Error: "+e1);
		}
	}
}
