package iot.mqtt;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import iot.dao.DAO;
import iot.models.DeviceModel;
import iot.models.StateModel;

public class MqttServerReceive{
	//final String mqttServer = "tcp://localhost:1883";
	final String mqttServer = "tcp://52.88.194.67:1883";
	final String clientId = "serverReceive";
	StateModel stateObj;
	MqttClient client;
	DAO dao = new DAO();
	Timer timer = new Timer();
	ArrayList<DeviceModel> pubDevices;
	// Hash maps to compare between updates
	Map<String, String> currentTopicsMap = new HashMap<String, String>();
	Map<String, String> previousTopicsMap;
	// Time in seconds to update subscriptions
	int refresh = 120;

	public MqttServerReceive() throws MqttException{
		this.connect();
	}

	/**
	 * execute update when timer runs out
	 */
	class RefreshTask extends TimerTask {
		public void run() {
			try {
				addTopics();
			} catch (MqttException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Connect to mqtt server
	 * @throws MqttException 
	 */
	public void connect() throws MqttException {
//			client = new MqttClient(mqttServer, clientId, new MemoryPersistence());			
//			// Add topics to subscribe to
//			addTopics();
//			client.connect();
//			client.setCallback(callback);
			
			
			try {
				client = new MqttClient(mqttServer, clientId, new MemoryPersistence());
				client.connect();
				// Add topics to subscribe to
				addTopics();
			} catch (MqttException e) {
				e.printStackTrace();
			}
			client.setCallback(callback);
	}


	/**
	 * @throws MqttException
	 */
	public void addTopics() throws MqttException {		
		// Get a list of currently connected publishing devices
		pubDevices = new ArrayList<DeviceModel>(dao.getPubDeviceInfo());
		String currentTopic;
		previousTopicsMap = new HashMap<String, String>(currentTopicsMap);
		currentTopicsMap.clear();
		
		if (client.isConnected()== false){
			this.connect();
		}

		// If no devices online no point in running
		if (pubDevices != null){
			for (DeviceModel devices: pubDevices) {
				currentTopic = devices.getHouseID()+"/"+devices.getLocation()+"/"+devices.getType()+"/"+devices.getName();
				currentTopicsMap.put(currentTopic, devices.getDeviceID());
				client.subscribe(currentTopic);
			}
		}
		// Loop through hash maps of topics to see if we can stop listening for a device
		Iterator<Entry<String, String>> it = previousTopicsMap.entrySet().iterator();
		while (it.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry pair = (Map.Entry)it.next();
			String topic = (String) pair.getKey();
			if (currentTopicsMap.containsKey(topic) == false){
				client.unsubscribe(topic);
			}
			it.remove(); // avoids a ConcurrentModificationException
		}
		// Re-set the timer
		timer.schedule(new RefreshTask(), refresh * 1000);
	}

	MqttCallback callback = new MqttCallback() {
		/* (non-Javadoc)
		 * @see org.eclipse.paho.client.mqttv3.MqttCallback#connectionLost(java.lang.Throwable)
		 */
		public void connectionLost(Throwable t) {
			// If connection is lost make sure to re-connect
			try {
				connect();
			} catch (MqttException e) {
				e.printStackTrace();
			}
		} 

		/* (non-Javadoc)
		 * @see org.eclipse.paho.client.mqttv3.MqttCallback#messageArrived(java.lang.String, org.eclipse.paho.client.mqttv3.MqttMessage)
		 */
		public void messageArrived(String topic, MqttMessage message) {
			String state = new String(message.getPayload());
			// Loop through objects to find who sent the message
			for (DeviceModel devices: pubDevices) {
				if (devices.getDeviceID() == currentTopicsMap.get(topic)){
					// Update DB
					StateModel stateObj = new StateModel(devices.getDeviceID(), state, new Date());
					devices.setCurrentState(state);
					dao.updateState(stateObj);
					dao.registerDevice(devices);
					// Break out of loop once found
					break;
				}
			}

		}

		/* (non-Javadoc)
		 * @see org.eclipse.paho.client.mqttv3.MqttCallback#deliveryComplete(org.eclipse.paho.client.mqttv3.IMqttDeliveryToken)
		 */
		public void deliveryComplete(IMqttDeliveryToken token) {
		}
	};
}
