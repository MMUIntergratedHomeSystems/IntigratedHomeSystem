package IOTProject.Devices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.phidgets.InterfaceKitPhidget;
import com.phidgets.PhidgetException;

public class InterfaceBoard implements MqttCallback {
	static String mqttServer = "tcp://52.88.194.67:1883";
	static InterfaceKitPhidget interfaceKit;
	private static ArrayList<String> clientListLeds = new ArrayList<String>();
	private static ArrayList<String> clientListActuators = new ArrayList<String>();
	Timer timer = new Timer();
	// 5 mins
	int refresh = 300;

	public static void main(String[] args) throws PhidgetException, IOException, MqttException{
		new InterfaceBoard();
	}

	public InterfaceBoard() throws PhidgetException, IOException, MqttException{
		// open interface board and start listening for sensor input
		interfaceKit = new InterfaceKitPhidget();
		interfaceKit.openAny();
		interfaceKit.waitForAttachment();

		// Add LEDS deviceID's
		clientListLeds.add("whitelight1");
		clientListLeds.add("yellowlight1");	
		clientListLeds.add("greenlight1");

		// Add Actuator deviceID's
		clientListActuators.add("thermostat1");
		clientListActuators.add("thermostat2");

		// Initialise LEDS 
		for (int i=0; i<clientListLeds.size(); i++){
			System.out.println(clientListLeds.get(i));
			// Get the registered information for the device
			DeviceObject device = DeviceUtils.getinfo(clientListLeds.get(i));

			// Check that it is registered
			if (device != null){
				String topic = device.houseID+"/"+device.location+"/"+device.type+"/"+device.name;
				System.out.println("Topic: "+topic);
				startMqttLeds(topic, clientListLeds.get(i));
			} else {
				System.out.println("Device "+clientListLeds.get(i)+" not registerd");
			}
		}

		// Initialise actuators
		for (int i=0; i<clientListActuators.size(); i++){
			System.out.println(clientListActuators.get(i));
			// Get the registered information for the device
			DeviceObject device = DeviceUtils.getinfo(clientListActuators.get(i));

			// Check that it is registered
			if (device != null){
				String topic = device.houseID+"/"+device.location+"/"+device.type+"/"+device.name;
				System.out.println("Topic: "+topic);
				startMqttActuator(topic, clientListActuators.get(i));
			} else {
				System.out.println("Device "+clientListActuators.get(i)+" not registerd");
			}
		}
	}
	
	// Timer for sending actuator values
	class UpdateTask extends TimerTask {
		private final int inputPort;
		private final String topic;
		private final MqttClient client;
		
		UpdateTask(int inputPort, String topic, MqttClient client){
			this.inputPort = inputPort;
			this.topic = topic;
			this.client = client;
		}
		
		public void run() {
			try {
				updateActuators(inputPort, topic, client);
			} catch (MqttPersistenceException e) {
				e.printStackTrace();
			} catch (PhidgetException e) {
				e.printStackTrace();
			} catch (MqttException e) {
				e.printStackTrace();
			}
		}
	}	

	private void startMqttActuator(String topic, String clientID) throws MqttException, PhidgetException {
		int input = clientListActuators.indexOf(clientID);
		MqttClient client = new MqttClient( 
				mqttServer, //URI 
				clientID,
				new MemoryPersistence()); //Persistence
		client.setCallback(this);
		client.connect();
		System.out.println("conneted to "+mqttServer+" "+client.isConnected());
		updateActuators(input, topic, client);
	}

	public void updateActuators(int inputPort, String topic, MqttClient client) throws PhidgetException, MqttPersistenceException, MqttException {	
				System.out.println(clientListActuators.get(inputPort)+": "+interfaceKit.getSensorValue(inputPort));
				MqttTopic mqttAddress = client.getTopic(topic);
				// Publish the message				
				MqttMessage message = new MqttMessage(String.valueOf(interfaceKit.getSensorValue(inputPort)).getBytes());
				message.setQos(0);
				// Retain the message
				mqttAddress.publish(message);
				
				timer.schedule(new UpdateTask(inputPort, topic, client), refresh * 1000);
	}

	/**
	 * Used for starting LED devices
	 * @param topic
	 * @param clientID
	 * @throws MqttException
	 */
	public static void startMqttLeds(String topic, String clientID) throws MqttException{
		final MqttClient client = new MqttClient( 
				mqttServer, //URI 
				clientID,
				new MemoryPersistence()); //Persistence
		client.connect();
		client.subscribe(topic, 1);	

		System.out.println("conneted to "+mqttServer+" "+client.isConnected());

		client.setCallback(new MqttCallback() {

			public void connectionLost(Throwable cause) {
				System.out.println("connection lost "+cause);
				cause.printStackTrace();
			}

			public void messageArrived(String topic, MqttMessage message) throws Exception, PhidgetException {
				String payload = new String(message.getPayload());
				System.out.println(clientListLeds.indexOf(client.getClientId()));
				System.out.println(client.getClientId());
				System.out.println("Message arrived: "+payload);
				if (payload.equals("0")){
					System.out.println("off");
					interfaceKit.setOutputState(clientListLeds.indexOf(client.getClientId()),false);
				} else if (payload.equals("1")){
					System.out.println("on");
					interfaceKit.setOutputState(clientListLeds.indexOf(client.getClientId()),true);
				} else {
					System.out.println("Unknown state");
				}
			}

			public void deliveryComplete(IMqttDeliveryToken token) {
				// not needed
			}
		});
	}

	public void connectionLost(Throwable cause) {
		// not needed
	}

	public void messageArrived(String topic, MqttMessage message) throws Exception {
		// not needed
	}

	public void deliveryComplete(IMqttDeliveryToken token) {
		// not needed
	}


}