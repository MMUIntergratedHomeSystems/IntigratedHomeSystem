package integratedhome.tom.mmu.integratedhomesystem;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class IHS_Api {

    private static String URL_BASE = "http://52.88.194.67:8080/IOTProjectServer/";
    private JSONParser jp;
    private UIController uiController;

    public IHS_Api(UIController uic) {
        jp = new JSONParser();
        this.uiController = uic;
    }



    //Callable methods
    public void getDevicesList() {
        new GetDevicesRequest().execute();
    }

    public void setState(String deviceID, boolean newState) {
        new SetDeviceRequest().execute(deviceID, newState ? "1" : "0");
    }

    public void removeDevice(String deviceID) {
        new RemoveDeviceRequest().execute(deviceID);
    }

    public void addDevice(String device_name, String device_manufacturer, String device_location, String device_type, String device_id, String house_id) {
        new AddDeviceRequest().execute(device_id, device_name, device_manufacturer, device_location, device_type, house_id);
    }

    //Async Tasks, called by the public methods, because performing network tasks on the main thread is bad
    private class GetDevicesRequest extends AsyncTask<String, String, JSONArray> {
        @Override
        protected JSONArray doInBackground(String... params) {
            String url = URL_BASE + "getDevice";
            HashMap<String, String> getparams = new HashMap<String, String>();
            return jp.makeHttpRequestForArray(url, "GET", getparams);
        }

        @Override
        protected void onPostExecute(JSONArray json) {
            uiController.updateDevicesList(json);

        }
    }

    private class SetDeviceRequest extends AsyncTask<String, String, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            String url = URL_BASE + "setState";
            HashMap<String, String> getparams = new HashMap<>();
            getparams.put("deviceID", params[0]);
            getparams.put("state", params[1]);
            return jp.makeHttpRequestForObject(url, "GET", getparams);
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                boolean success = json.getBoolean("sucsess");
                if (!success) {
                    String message = json.getString("message");
                    uiController.makeError(message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            getDevicesList();
        }
    }

    private class RemoveDeviceRequest extends AsyncTask<String, String, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            String url = URL_BASE + "removeDevice";
            HashMap<String, String> getparams = new HashMap<>();
            getparams.put("deviceID", params[0]);
            return jp.makeHttpRequestForObject(url, "GET", getparams);
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            getDevicesList();
        }
    }
    private class AddDeviceRequest extends AsyncTask<String, String, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            String url = URL_BASE + "registerDevice";
            HashMap<String, String> getparams = new HashMap<>();

            getparams.put("deviceID", params[0]);
            getparams.put("name", params[1]);
            getparams.put("manufacturer", params[2]);
            getparams.put("location", params[3]);
            getparams.put("type", params[4]);
            getparams.put("houseID", params[5]);

            getparams.put("connected", "false");
            getparams.put("currentState", "0");

            return jp.makeHttpRequestForObject(url, "GET", getparams);
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            getDevicesList();
        }
    }
}
