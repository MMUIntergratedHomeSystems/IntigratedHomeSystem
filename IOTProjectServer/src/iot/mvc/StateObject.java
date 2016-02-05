package iot.mvc;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

@Document()
public class StateObject {
	String deviceID;
	String state;
	Date dateStored;
	
	public StateObject(String deviceID, String state, Date dateStored) {
		this.deviceID = deviceID;
		this.state = state;
		this.dateStored = dateStored;
	}

	public String getDeviceID() {
		return deviceID;
	}
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Date getDateStored() {
		return dateStored;
	}
	public void setDateStored(Date dateStored) {
		this.dateStored = dateStored;
	}

	@Override
	public String toString() {
		return "StateObject [deviceID=" + deviceID + ", state=" + state + ", dateStored=" + dateStored + "]";
	}
}
