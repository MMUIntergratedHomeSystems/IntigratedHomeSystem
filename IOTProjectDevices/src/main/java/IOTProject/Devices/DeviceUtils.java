package IOTProject.Devices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;

public class DeviceUtils {
	static String baseUrl = "http://52.88.194.67:8080/IOTProjectServer";  
	static Gson gson = new Gson();
	static DeviceObject device;
	
	/**
	 * Method to connected to the database to retrieve information about the device.
	 * @param deviceID - Device ID to search for.
	 * @return - DeviceModel
	 * @throws IOException
	 */
	public static DeviceObject getinfo(String deviceID) throws IOException
	{
		
		String getDeviceUrl = baseUrl+"/getDevice?deviceID="+deviceID;
		URL request;

			request = new URL(getDeviceUrl);
			HttpURLConnection con = (HttpURLConnection) request.openConnection();

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
			device = gson.fromJson(response.toString(), DeviceObject.class);
			
		return device;		
	}
}
