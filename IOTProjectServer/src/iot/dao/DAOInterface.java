package iot.dao;
import java.util.List;

import iot.models.DeviceModel;
import iot.models.ResponseModel;
import iot.models.StateModel;


/**
 * Interface for the DAO
 *
 */
public interface DAOInterface {

	public List<DeviceModel> getPubDeviceInfo();
	public List<DeviceModel> getAllDeviceInfo();
	public DeviceModel getDeviceInfo(String deviceID);
	public ResponseModel registerDevice(DeviceModel device);
	public ResponseModel removeDevice(String deviceID);
	public ResponseModel updateState(StateModel state);
	public List<StateModel> getStateInfo(String deviceID);
	public List<StateModel> getAllStateInfo();
	public StateModel getLastStateInfo(String deviceID);
	public List<StateModel> getAllLastStateInfo();


}
