package integratedhome.tom.mmu.integratedhomesystem;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import java.util.HashMap;

/**
 * Created by Tom on 04/04/2016.
 */
public class IHS_Api {

    private static String URL_BASE = "http://52.88.194.67:8080/IOTProjectServer/";
    private JSONParser jp;

    public IHS_Api() {
        //creates new JSON parser
        jp = new JSONParser();
    }


    //Callable methods
    public void getDevicesList() {
        //gets all devices currently located on te server.
        new GetDevicesRequest().execute();
    }

    public void setState(String deviceID, boolean newState) {
        //sets the state of a selected device and sends the deviceID and it's new state.
        new SetDeviceRequest().execute(deviceID, newState ? "1" : "0");
    }

    public void removeDevice(String deviceID) {
        //deletes a selected device.
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
            Log.v("IHS", json.toString());
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
            Log.v("IHS", json.toString());
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
            Log.v("IHS", json.toString());
        }
    }
}
