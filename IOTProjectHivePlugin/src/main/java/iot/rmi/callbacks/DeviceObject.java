package iot.rmi.callbacks;


public class DeviceObject {
	String deviceID;
	String houseID;
	String name;
	String manufacturer;
	String location;
	String type;
	boolean connected;
	String currentState;

	public DeviceObject(String deviceID, String houseID, String name, String manufacturer, String location, String type,
			boolean connected, String currentState) {
		super();
		this.deviceID = deviceID;
		this.houseID = houseID;
		this.name = name;
		this.manufacturer = manufacturer;
		this.location = location;
		this.type = type;
		this.connected = connected;
		this.currentState = currentState;
	}

	public String getHouseID() {
		return houseID;
	}

	public void setHouseID(String houseID) {
		this.houseID = houseID;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public String isCurrentState() {
		return currentState;
	}

	public void setCurrentState(String currentState) {
		this.currentState = currentState;
	}

	public String getDeviceID() {
		return deviceID;
	}
	
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getManufacturer() {
		return manufacturer;
	}
	
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "DeviceObject [deviceID=" + deviceID + ", houseID=" + houseID + ", name=" + name + ", manufacturer="
				+ manufacturer + ", location=" + location + ", type=" + type + ", connected=" + connected
				+ ", currentState=" + currentState + "]";
	}

}
