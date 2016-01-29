package iot.mvc;

import java.util.Date;

public class StateObject {
	String State;
	Date DateStored;

	public String getState() {
		return State;
	}
	public void setState(String state) {
		State = state;
	}
	public Date getDateStored() {
		return DateStored;
	}
	public void setDateStored(Date dateStored) {
		DateStored = dateStored;
	}
	
	@Override
	public String toString() {
		return "StateObject [State=" + State + ", DateStored=" + DateStored + "]";
	}

}
