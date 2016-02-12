package mmu.tom.linkedviewproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Tom on 12/02/2016.
 */
public class DeviceDetailsActivity extends AppCompatActivity {

    private EditText address;
    private EditText name;
    private EditText manufacturer;
    private EditText location;
    private EditText type;

    private int DeviceID;

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

        setContentView(R.layout.activity_device_details);

        this.address = (EditText) this.findViewById(R.id.edit_address);
        this.name = (EditText) this.findViewById(R.id.edit_name);
        this.manufacturer = (EditText) this.findViewById(R.id.edit_manufacturer);
        this.location = (EditText) this.findViewById(R.id.edit_location);
        this.type = (EditText) this.findViewById(R.id.edit_type);


        this.DeviceID = getIntent().getIntExtra("DeviceID", -1);
        if (this.DeviceID >0){
            // have a customerID passed properly
            new GetDeviceDetails().execute(new ApiConnector());


        }
    }

    private class GetDeviceDetails extends AsyncTask<ApiConnector,Long,JSONArray>
    {
        @Override
        protected JSONArray doInBackground(ApiConnector... params) {

            // it is executed on Background thread

            return params[0].GetDeviceDetails(DeviceID);
        }

        @Override
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
    }


}
