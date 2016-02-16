package iot.mvc;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Device")
public class DeviceObject {
	@Id 
	String deviceID;
	String name;
	String manufacturer;
	String location;
	String type;

	public DeviceObject(String deviceID,
			String name,
			String manufacturer,
			String location,
			String type
			){		
		this.deviceID = deviceID;
		this.name = name;
		this.manufacturer = manufacturer;
		this.location = location;
		this.type = type;
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
		return "DeviceObject [deviceID=" + deviceID + ", name=" + name + ", manufacturer="
				+ manufacturer + ", location=" + location + ", type=" + type + "]";
	}

}
