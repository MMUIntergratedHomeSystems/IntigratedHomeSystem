package iot.mqtt;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import iot.dao.DAO;
import iot.mvc.DeviceObject;
import iot.rmi.RMIClient;

public class Set implements MqttCallback {
	//final String mqttServer = "tcp://localhost:1883";
	final String mqttServer = "tcp://52.88.194.67:1883";
	final String clientId = "server";

	public Set(){
	}

	public void send(String deviceID, String state){
		DAO dao = new DAO();
		RMIClient rmiClient = new RMIClient();
		try {
			// Check that the device is registered
			if (dao.getDeviceInfo(deviceID)!=null){
				DeviceObject device = dao.getDeviceInfo(deviceID);
				// Check the device is connected to the MQTT server
				if (rmiClient.isDeviceConncted(deviceID)){
					String location = device.getLocation();
					String type = device.getType();
					String name = device.getName();
					String topic = location+"/"+type+"/"+name;

					System.out.println(topic);

					// Connect to MQTT
					MqttClient serverClient = new MqttClient(
							mqttServer,
							clientId, 
							new MemoryPersistence());
					serverClient.setCallback(this);
					serverClient.connect();
					MqttTopic mqttAddress = serverClient.getTopic(topic);
					//serverClient.setCallback((MqttCallback) this);
					// Publish the message
					MqttMessage message = new MqttMessage(state.getBytes());
					message.setQos(0);

					IMqttDeliveryToken token = null;
					token = mqttAddress.publish(message);

					System.out.println("Message published");
					token.waitForCompletion();

					mqttAddress.publish(message);
					//	System.out.println("Token: "+token);
					//	serverClient.disconnect();
					//serverClient.close();
					//	System.out.println("Disconnected");
				} else {
					// TODO: return error - device not connected
				}
			} else {
				// TODO: return error - can't find device
			}

		} catch(MqttException | RemoteException | NotBoundException me) {
			System.out.println("reason "+((MqttException) me).getReasonCode());
			System.out.println("msg "+me.getMessage());
			System.out.println("loc "+me.getLocalizedMessage());
			System.out.println("cause "+me.getCause());
			System.out.println("excep "+me);
			me.printStackTrace();
		}
	}	

	/**
	 * 
	 * deliveryComplete
	 * This callback is invoked when a message published by this client
	 * is successfully received by the broker.
	 * 
	 */
	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		try {
			System.out.println("Pub complete" + new String(arg0.getMessage().getPayload()));
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	@Override
	public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("arrived");
	}

	@Override
	public void connectionLost(Throwable arg0) {
		// TODO Auto-generated method stub

	}
}
