package iot.test;
import java.util.Arrays;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Test {

	public static void main(String[] args) throws MqttException{
		System.out.println("working?");
		MqttClient client = new MqttClient( 
				"tcp://0.0.0.0:1883", //URI 
				MqttClient.generateClientId(), //ClientId 
				new MemoryPersistence()); //Persistence

	// LWT
//		MqttConnectOptions options = new MqttConnectOptions();
//
//		options.setWill( 
//				"topic", // topic 
//				"payload".getBytes(UTF_8), // payload 
//				2, // QoS 
//				false); // retained?
//		client.connect(options);
		
		// User pass
//		MqttConnectOptions options = new MqttConnectOptions();
//		options.setUserName("username");
//		options.setPassword("password".toCharArray());
//		client.connect(options);

		

	//	client.connect();
	//	System.out.println(client.isConnected());

		
		// Sub
		client.setCallback(new MqttCallback() {
//			 
            @Override
            public void connectionLost(Throwable cause) { //Called when the client lost the connection to the broker 
            }
 
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                System.out.println(topic + ": " + Arrays.toString(message.getPayload()));
            }
 
            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {//Called when a outgoing publish is complete 
            }

        });
        
client.connect();
client.subscribe("home/things/TestingClientID", 1);
System.out.println(client.isConnected());


//Publish		
client.publish( 
	    "topic", // topic 
	    "payload".getBytes(), // payload 
	    2, // QoS 
	    false); // retained? 
		// unsub
		// 	client.unsubscribe("#");
		
		
		// www.hivemq.com/blog/mqtt-client-library-encyclopedia-eclipse-paho-java
	}

}