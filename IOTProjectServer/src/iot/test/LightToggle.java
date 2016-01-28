package iot.test;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class LightToggle {

	// http://www.eclipse.org/paho/files/javadoc/index.html
	public static void main(String[] args) throws MqttException{
		MqttClient client = new MqttClient( 
				"tcp://localhost:1883", //URI 
				"lightTog", //ClientId 
				new MemoryPersistence()); //Persistence

		client.connect();
		client.subscribe("home/things/light", 1);
		System.out.println("connected: "+client.isConnected());

		client.setCallback(new MqttCallback() {			 
			@Override
			public void connectionLost(Throwable cause) {
				System.out.println("connection lost");
			}

			@Override
			public void messageArrived(String topic, MqttMessage message) throws Exception {
				
				System.out.println(topic + ": " + new String(message.getPayload()));
				String payload = new String(message.getPayload());
				
				if (payload.equals("on")){
					System.out.println("Light On");
				} else if (payload.equals("off")) {
					System.out.println("Light Off");
				} else {
					System.out.println("?");
				}
// http://www.ibm.com/developerworks/cloud/library/cl-mqtt-bluemix-iot-node-red-app/
			}

			@Override
			public void deliveryComplete(IMqttDeliveryToken token) {//Called when a outgoing publish is complete 
			}
		});

	}

}
