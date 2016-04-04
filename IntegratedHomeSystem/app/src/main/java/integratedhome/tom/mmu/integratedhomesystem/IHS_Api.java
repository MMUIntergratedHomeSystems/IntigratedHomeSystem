package integratedhome.tom.mmu.integratedhomesystem;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
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

    private class SetDeviceRequest extends AsyncTask<String, String, JSONArray> {
        @Override
        protected JSONArray doInBackground(String... params) {
            String url = URL_BASE + "setState";
            HashMap<String, String> getparams = new HashMap<String, String>();
            getparams.put("deviceID", params[0]);
            getparams.put("state", params[1]);
            return jp.makeHttpRequestForArray(url, "GET", getparams);
        }

        @Override
        protected void onPostExecute(JSONArray json) {
            getDevicesList();
        }
    }

    private class RemoveDeviceRequest extends AsyncTask<String, String, JSONArray> {
        @Override
        protected JSONArray doInBackground(String... params) {
            String url = URL_BASE + "removeDevice";
            HashMap<String, String> getparams = new HashMap<String, String>();
            getparams.put("deviceID", params[0]);
            return jp.makeHttpRequestForArray(url, "GET", getparams);
        }

        @Override
        protected void onPostExecute(JSONArray json) {
            getDevicesList();
        }
    }
}
