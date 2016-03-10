package iot.mqtt;

public class ResponseModel {
	Boolean sucsess;
	String message;
	
	public ResponseModel(Boolean sucsess, String message) {
		super();
		this.sucsess = sucsess;
		this.message = message;
	}

	public Boolean getSucsess() {
		return sucsess;
	}

	public void setSucsess(Boolean sucsess) {
		this.sucsess = sucsess;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return "ResponseModel [sucsess=" + sucsess + ", message=" + message + "]";
	}

}
