package iot.mqtt;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Date;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import iot.dao.DAO;
import iot.models.DeviceModel;
import iot.models.ResponseModel;
import iot.models.StateModel;
import iot.rmi.RMIClient;

public class MqttServerSend implements MqttCallback {
	final String mqttServer = "tcp://localhost:1883";
	//final String mqttServer = "tcp://52.88.194.67:1883";
	final String clientId = "serverSend";
	final int qos = 0;
	public ResponseModel responce;
	public DeviceModel device;

	public MqttServerSend(){
		super();
	}

	/**
	 * Takes a deviceID and a state to send to a device connected to the MQTT server, returns a ResponceModel with success and message.
	 * @param deviceID
	 * @param state
	 * @return response
	 */
	public ResponseModel send(String deviceID, String state){
		responce = new ResponseModel(false, deviceID+": Unknown Error");
		DAO dao = new DAO();
		RMIClient rmiClient = new RMIClient();
		try {
			// Check that the device is registered
			// and maybe add a registered boolean?
			if (dao.getDeviceInfo(deviceID)!=null){
				device = dao.getDeviceInfo(deviceID);
				if (state != null){
					// Check the device is connected to the MQTT server
					if (rmiClient.isDeviceConncted(deviceID)){
						// Construct the MQTT address
						String houseID = device.getHouseID();
						String location = device.getLocation();
						String type = device.getType();
						String name = device.getName();
						String topic = houseID+"/"+location+"/"+type+"/"+name;

						// Connect to MQTT
						MqttClient serverClient = new MqttClient(
								mqttServer,
								clientId, 
								new MemoryPersistence());
						serverClient.setCallback(this);
						serverClient.connect();
						MqttTopic mqttAddress = serverClient.getTopic(topic);

						// Publish the message
						MqttMessage message = new MqttMessage(state.getBytes());
						message.setQos(qos);
						// Retain the message
						message.setRetained(true);
						IMqttDeliveryToken token = null;
						token = mqttAddress.publish(message);
						// Wait for message to arrive - this will call delivery complete
						token.waitForCompletion();
						
						if (token.isComplete() == true){
							// Update the device object and state
							StateModel stateObj = new StateModel(device.getDeviceID(), state, new Date());
							device.setCurrentState(state);
							dao.updateState(stateObj);
							dao.registerDevice(device);

							// Update the response
							responce.setSucsess(true);
							responce.setMessage(device.getDeviceID()+": Request sent sucsessfuly");
						} else {
							responce.setSucsess(false);
							responce.setMessage(device.getDeviceID()+": Unknwon error: "+token.getException());
						}
					} else {
						responce.setSucsess(false);
						responce.setMessage(device.getName()+": Device is not connected");
					}
				} else {
					responce.setSucsess(false);
					responce.setMessage(device.getName()+": No state specified");
				}
			} else {
				responce.setSucsess(false);
				responce.setMessage(device.getName()+": Device is not registerd");
			}

		} catch(MqttException | RemoteException | NotBoundException me) {
			System.out.println("reason "+((MqttException) me).getReasonCode());
			System.out.println("msg "+me.getMessage());
			System.out.println("loc "+me.getLocalizedMessage());
			System.out.println("cause "+me.getCause());
			System.out.println("excep "+me);
			me.printStackTrace();
			
			responce.setSucsess(false);
			responce.setMessage(device.getName()+": Unknown error "+me);
		}
		return responce;
	}	

	/* (non-Javadoc)
	 * @see org.eclipse.paho.client.mqttv3.MqttCallback#deliveryComplete(org.eclipse.paho.client.mqttv3.IMqttDeliveryToken)
	 */
	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		try {
			System.out.println("Pub complete" + new String(arg0.getMessage().getPayload()));
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.paho.client.mqttv3.MqttCallback#messageArrived(java.lang.String, org.eclipse.paho.client.mqttv3.MqttMessage)
	 */
	@Override
	public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.paho.client.mqttv3.MqttCallback#connectionLost(java.lang.Throwable)
	 */
	@Override
	public void connectionLost(Throwable arg0) {
	}
}
