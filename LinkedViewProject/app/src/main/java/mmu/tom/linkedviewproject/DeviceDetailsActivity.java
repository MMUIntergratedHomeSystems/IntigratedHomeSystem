package mmu.tom.linkedviewproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Tom on 12/02/2016.
 */
public class DeviceDetailsActivity extends AppCompatActivity {

    private EditText address;
    private EditText name;
    private EditText manufacturer;
    private EditText location;
    private EditText type;
    private EditText deviceID;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_details);

        ImageButton button1 = (ImageButton) findViewById(R.id.image_button_back);
        button1.setOnClickListener(new View.OnClickListener() {
            Class ourClass;

            public void onClick(View v) {

                Intent intent = new Intent(DeviceDetailsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        Button submitButton = (Button) findViewById(R.id.submit_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            Class ourClass;

            public void onClick(View v) {

                sendDeviceDetails();
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

    public JSONArray sendDeviceDetails() {
        // URL for getting all customers


        String url = "http://52.88.194.67:8080/IOTProjectServer/registerDevice?";

        // Get HttpResponse Object from url.
        // Get HttpEntity from Http Response Object

        HttpEntity httpEntity = null;

        try {

            DefaultHttpClient httpClient = new DefaultHttpClient();  // Default HttpClient
            HttpGet httpGet = new HttpGet(url);

            HttpResponse httpResponse = httpClient.execute(httpGet);

            httpEntity = httpResponse.getEntity();


        } catch (ClientProtocolException e) {

            // Signals error in http protocol
            e.printStackTrace();

            //Log Errors Here


        } catch (IOException e) {
            e.printStackTrace();
        }


        // Convert HttpEntity into JSON Array
        JSONArray jsonArray = null;
        if (httpEntity != null) {
            try {
                String entityResponse = EntityUtils.toString(httpEntity);
                Log.e("Entity Response  : ", entityResponse);

                jsonArray = new JSONArray(entityResponse);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return jsonArray;


    }


}
