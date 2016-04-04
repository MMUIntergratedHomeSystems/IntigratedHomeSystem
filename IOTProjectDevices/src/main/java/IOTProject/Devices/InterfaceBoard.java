package IOTProject.Devices;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.phidgets.InterfaceKitPhidget;
import com.phidgets.PhidgetException;
import com.phidgets.event.AttachEvent;
import com.phidgets.event.AttachListener;
import com.phidgets.event.DetachEvent;
import com.phidgets.event.DetachListener;
import com.phidgets.event.ErrorEvent;
import com.phidgets.event.ErrorListener;
import com.phidgets.event.InputChangeEvent;
import com.phidgets.event.InputChangeListener;
import com.phidgets.event.OutputChangeEvent;
import com.phidgets.event.OutputChangeListener;
import com.phidgets.event.SensorChangeEvent;
import com.phidgets.event.SensorChangeListener;


public class InterfaceBoard implements SensorChangeListener, InputChangeListener, AttachListener, DetachListener, ErrorListener,
OutputChangeListener{
	static String mqttServer = "tcp://52.88.194.67:1883";
	static InterfaceKitPhidget interfaceKit;
	private static ArrayList<String> clientList = new ArrayList<String>();

	public static void main(String[] args) throws PhidgetException, IOException, MqttException{
		new InterfaceBoard();
	}
	
	
	public InterfaceBoard() throws PhidgetException, IOException, MqttException{
		// start listening to the inputs to the board
		// Detects when sensor is added/removed or when new value is
		// detected
		interfaceKit = new InterfaceKitPhidget();
		interfaceKit.addAttachListener(this);
		interfaceKit.addDetachListener(this);
		interfaceKit.addSensorChangeListener(this);
		interfaceKit.addInputChangeListener(this);
		interfaceKit.addOutputChangeListener(this);

		// open interface board and start listening for sensor input
		interfaceKit.openAny();
		interfaceKit.waitForAttachment();
		
		clientList.add("whitelight1");
		clientList.add("yellowlight1");	
		clientList.add("greenlight1");
		
		for (int i=0; i<clientList.size(); i++){
			System.out.println(clientList.get(i));
			// Get the registered information for the device
			DeviceObject device = DeviceUtils.getinfo(clientList.get(i));

			// Check that it is registered
			if (device != null){
				String topic = device.houseID+"/"+device.location+"/"+device.type+"/"+device.name;
				System.out.println("Topic: "+topic);
				startMqtt(topic, clientList.get(i));
			} else {
				System.out.println("Device "+clientList.get(i)+" not registerd");
			}
		}
	}

	public static void startMqtt(String topic, String clientID) throws MqttException{
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
				System.out.println(clientList.indexOf(client.getClientId()));
				System.out.println(client.getClientId());
				System.out.println("Message arrived: "+payload);
				if (payload.equals("0")){
					System.out.println("off");
					interfaceKit.setOutputState(clientList.indexOf(client.getClientId()),false);
				} else if (payload.equals("1")){
					System.out.println("on");
					interfaceKit.setOutputState(clientList.indexOf(client.getClientId()),true);
				} else {
					System.out.println("Unknown state");
				}
			}

			public void deliveryComplete(IMqttDeliveryToken token) {
				//Called when a outgoing publish is complete 
				System.out.println("arrived");
			}
		});
	}

	public void outputChanged(OutputChangeEvent arg0) {
		System.out.println(arg0);
	}

	public void error(ErrorEvent arg0) {
		System.out.println(arg0);
	}

	public void detached(DetachEvent arg0) {
		System.out.println(arg0);
	}

	public void attached(AttachEvent arg0) {
		System.out.println(arg0);
	}

	public void inputChanged(InputChangeEvent arg0) {
		System.out.println(arg0);
	}

	public void sensorChanged(SensorChangeEvent arg0) {
		System.out.println(arg0);
	}
}