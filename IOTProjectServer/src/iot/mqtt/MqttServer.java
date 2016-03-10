package iot.mqtt;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import iot.dao.DAO;
import iot.mvc.DeviceObject;
import iot.rmi.RMIClient;

public class MqttServer implements MqttCallback {
	//final String mqttServer = "tcp://localhost:1883";
	final String mqttServer = "tcp://52.88.194.67:1883";
	final String clientId = "server";
	final int qos = 0;
	ResponseModel responce;

	public MqttServer(){
	}

	public ResponseModel send(String deviceID, String state){
		responce = new ResponseModel(false, deviceID+": Unknown Error");
		DAO dao = new DAO();
		RMIClient rmiClient = new RMIClient();
		try {
			// Check that the device is registered
			// and maybe add a registered boolean?
			if (dao.getDeviceInfo(deviceID)!=null){
				DeviceObject device = dao.getDeviceInfo(deviceID);
				// Check the device is connected to the MQTT server
				if (rmiClient.isDeviceConncted(deviceID)){
					String houseID = device.getHouseID();
					String location = device.getLocation();
					String type = device.getType();
					String name = device.getName();
					String topic = houseID+"/"+location+"/"+type+"/"+name;

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
					message.setQos(qos);

					IMqttDeliveryToken token = null;
					token = mqttAddress.publish(message);

					System.out.println("Message published");
					token.waitForCompletion();

										
					//	System.out.println("Token: "+token);
					//	serverClient.disconnect();
					//serverClient.close();
					//	System.out.println("Disconnected");
				} else {
					responce.setSucsess(false);
					responce.setMessage(device.getName()+" is not connected");
				}
			} else {
				responce.setSucsess(false);
				responce.setMessage("Device is not registerd");
			}

		} catch(MqttException | RemoteException | NotBoundException me) {
			System.out.println("reason "+((MqttException) me).getReasonCode());
			System.out.println("msg "+me.getMessage());
			System.out.println("loc "+me.getLocalizedMessage());
			System.out.println("cause "+me.getCause());
			System.out.println("excep "+me);
			me.printStackTrace();
		}
		return responce;
	}	

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		try {
			System.out.println("Pub complete" + new String(arg0.getMessage().getPayload()));
			responce.setSucsess(true);
			// TODO: Update object
			// TODO: Update state log 
		} catch (MqttException e) {
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
