package iot.models;

public class ResponseModel {
	Boolean success;
	String message;
	
	public ResponseModel(Boolean success, String message) {
		super();
		this.success = success;
		this.message = message;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return "ResponseModel [success=" + success + ", message=" + message + "]";
	}

}
