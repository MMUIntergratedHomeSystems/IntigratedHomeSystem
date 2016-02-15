package mmu.tom.linkedviewproject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Tom on 12/02/2016.
 */
public class DeviceDetailsActivity extends AppCompatActivity {

    private static final String TAG = "ShowDevice";

    private EditText address;
    private EditText name;
    private EditText manufacturer;
    private EditText location;
    private EditText type;
    private EditText deviceID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_details);

        Log.i(TAG, "  OpenedDeviceDetails");



        Button submitButton = (Button) findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i(TAG, "  Button prewssed");
                JSONObject postData = new JSONObject();

                Log.i(TAG," SubmitButtonClicked");
                try {
                    //set up the strings
                    postData.put("name", name.getText().toString());

                    postData.put("address", address.getText().toString());
                    postData.put("manufacturer", manufacturer.getText().toString());
                    postData.put("location", location.getText().toString());
                    postData.put("type", type.getText().toString());
                    postData.put("deviceID", deviceID.getText().toString());


                    //now to post the data
                    new SendDeviceDetails().execute("http://52.88.194.67:8080/IOTProjectServer/registerDevice", postData.toString());

                    Log.i(TAG, "See What's Sending:    " + postData.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        setContentView(R.layout.activity_device_details);

        this.address = (EditText) this.findViewById(R.id.edit_address);
        this.name = (EditText) this.findViewById(R.id.edit_name);
        this.manufacturer = (EditText) this.findViewById(R.id.edit_manufacturer);
        this.location = (EditText) this.findViewById(R.id.edit_location);
        this.type = (EditText) this.findViewById(R.id.edit_type);
        this.deviceID = (EditText) this.findViewById(R.id.edit_device_id);

    }




        protected void onPostExecute(JSONArray jsonArray) {

            try
            {
                JSONObject device = jsonArray.getJSONObject(0);

                name.setText(device.getString("name"));
                address.setText(device.getString("address"));
                location.setText(device.getString("location"));
                manufacturer.setText(device.getString("manufacturer"));
                type.setText(device.getString("type"));
            }
            catch(Exception e){
                e.printStackTrace();
            }



        }

    private class SendDeviceDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String data = "";

            HttpURLConnection httpURLConnection = null;
            try {

                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoOutput(true);

                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes("PostData=" + params[1]);
                wr.flush();
                wr.close();

                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);

                int inputStreamData = inputStreamReader.read();
                while (inputStreamData != -1) {
                    char current = (char) inputStreamData;
                    inputStreamData = inputStreamReader.read();
                    data += current;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }

            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("TAG", result); // this is expecting a response code to be sent from your server upon receiving the POST data
        }
    }

}
