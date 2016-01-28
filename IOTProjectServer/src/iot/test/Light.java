package iot.test;
import java.util.Arrays;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Light {

	// http://www.eclipse.org/paho/files/javadoc/index.html
	public static void main(String[] args) throws MqttException{
		MqttClient client = new MqttClient( 
				"tcp://0.0.0.0:1883", //URI 
				"light",//MqttClient.generateClientId(), //ClientId 
				new MemoryPersistence()); //Persistence

		client.connect();
		client.subscribe("home/things/light", 1);
		System.out.println(client.isConnected());

		client.setCallback(new MqttCallback() {			 
			@Override
			public void connectionLost(Throwable cause) {
				System.out.println("connection lost");
			}

			@Override
			public void messageArrived(String topic, MqttMessage message) throws Exception {
				String payload = new String(message.getPayload());
				
				if (payload.equals("on")){
					System.out.println("Light On");
				} else {
					System.out.println("Light Off");
				}

			}

			@Override
			public void deliveryComplete(IMqttDeliveryToken token) {//Called when a outgoing publish is complete 
				System.out.println("arrived");
			}
		});

	}

}
