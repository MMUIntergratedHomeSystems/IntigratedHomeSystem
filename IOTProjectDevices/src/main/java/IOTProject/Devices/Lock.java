package IOTProject.Devices;

import java.io.IOException;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.phidgets.AdvancedServoPhidget;
import com.phidgets.PhidgetException;
import com.phidgets.event.AttachEvent;
import com.phidgets.event.AttachListener;
import com.phidgets.event.DetachEvent;
import com.phidgets.event.DetachListener;
import com.phidgets.event.ErrorEvent;
import com.phidgets.event.ErrorListener;
import com.phidgets.event.ServoPositionChangeEvent;
import com.phidgets.event.ServoPositionChangeListener;

public class Lock {
	static AdvancedServoPhidget servo;
	static String clientID= "lock1";//MqttClient.generateClientId()
	static String mqttServer = "tcp://52.88.194.67:1883";
	static int servoNumber = 0;
	static DeviceObject device;

	public static void main(String[] args) throws IOException, PhidgetException, MqttException{
		// Create instance of the motor
		servo = new AdvancedServoPhidget();
		// Start the servo listener
		startServoListeners();
		// Get the registered information for the device
		device = DeviceUtils.getinfo(clientID);

		// Check that it is registered
		if (device != null){
			String topic = device.houseID+"/"+device.location+"/"+device.type+"/"+device.name;
			System.out.println("Topic: "+topic);
			startMqtt(topic);
		} else {
			System.out.println("Device "+clientID+" not registerd");
		}
	}
	
	public static void startMqtt(String topic) throws MqttException{
		MqttClient client = new MqttClient( 
				mqttServer, //URI 
				clientID,
				new MemoryPersistence()); //Persistence

		client.connect();
		client.subscribe(topic, 1);
		System.out.println("conneted to "+mqttServer+" "+client.isConnected());

		client.setCallback(new MqttCallback() {

			public void connectionLost(Throwable cause) {
				System.out.println("connection lost");
			}

			public void messageArrived(String topic, MqttMessage message) throws Exception {
				String payload = new String(message.getPayload());
				System.out.println("Message arrived: "+payload);
				if (payload.equals("0")){
					System.out.println("unlock");
					moveServoTo(0);
				} else if (payload.equals("1")){
					System.out.println("lock");
					moveServoTo(180);
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

	public static void moveServoTo(int position) {
		// Method to move motor to indicated position
		System.out.println("Move motor to "+position);
		try {
			servo.openAny();
			servo.waitForAttachment();
			servo.setEngaged(servoNumber, false);
			servo.setSpeedRampingOn(servoNumber, false);
			servo.setPosition(servoNumber, position);
			servo.setEngaged(servoNumber, true);
			servo.close();
		} catch (PhidgetException pe) {
			System.out.println("Motor error " + pe);
		}
	}
	
	private static void startServoListeners() {
		servo.addAttachListener(new AttachListener() {
			public void attached(AttachEvent ae) {
				System.out.println("attachment of " + ae);
			}
		});

		servo.addDetachListener(new DetachListener() {
			public void detached(DetachEvent ae) {
				System.out.println("detachment of " + ae);
			}
		});

		servo.addErrorListener(new ErrorListener() {
			public void error(ErrorEvent ee) {
				System.out.println("error event for " + ee);
			}
		});

		servo.addServoPositionChangeListener(new ServoPositionChangeListener() {
			public void servoPositionChanged(ServoPositionChangeEvent oe) {
				System.out.println(oe);
			}
		});

	}
}