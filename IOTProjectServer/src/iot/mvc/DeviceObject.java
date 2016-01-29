package iot.mvc;

import java.util.ArrayList;
import org.springframework.data.annotation.Id;

public class DeviceObject {
	@Id String DeviceID;
	String Address;
	String Name;
	String Manufacturer;
	ArrayList<StateObject> State;
	// Location
	// Type
	// Logic/Rules?
	
	public String getDeviceID() {
		return DeviceID;
	}
	public void setDeviceID(String deviceID) {
		DeviceID = deviceID;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getManufacturer() {
		return Manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		Manufacturer = manufacturer;
	}
	public ArrayList<StateObject> getState() {
		return State;
	}
	public void setState(ArrayList<StateObject> state) {
		State = state;
	}
	@Override
	public String toString() {
		return "DeviceObject [DeviceID=" + DeviceID + ", Address=" + Address + ", Name=" + Name + ", Manufacturer="
				+ Manufacturer + ", State=" + State + "]";
	}
}
