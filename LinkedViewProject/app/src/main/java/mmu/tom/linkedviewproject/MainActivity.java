package mmu.tom.linkedviewproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private ListView GetAllDevicesListView;
    private JSONArray jsonArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton button1 = (ImageButton) findViewById(R.id.image_button_new);

        button1.setOnClickListener(new View.OnClickListener() {
            Class ourClass;

            public void onClick(View v) {

                Intent i;
                i = new Intent(MainActivity.this, DeviceDetailsActivity.class);
                startActivity(i);
            }
        });

        this.GetAllDevicesListView = (ListView) this.findViewById(R.id.GetAllDevicesListView);

        new GetAllDevicesTask().execute(new ApiConnector());

        GetAllDevicesListView.setOnItemClickListener (new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){

                try

                {
                    //GetDevice that was clicked
                    JSONObject deviceClicked = jsonArray.getJSONObject(position);


                    //Send DeviceId
                    Intent showDetails = new Intent(getApplicationContext(), DeviceDetailsActivity.class);
                    showDetails.putExtra("deviceID", deviceClicked.getString("deviceID"));
                }

                catch(
                        JSONException e
                        )

                {
                    e.printStackTrace();
                }
            }
        });

    }

    public void setListAdapter(JSONArray jsonArray){

        this.jsonArray = jsonArray;
        this.GetAllDevicesListView.setAdapter((new GetAllDeviceListViewAdapter(jsonArray, this)));


    }

    private class GetAllDevicesTask extends AsyncTask<ApiConnector,Long,JSONArray>
    {
        @Override
        protected JSONArray doInBackground(ApiConnector... params) {

            // it is executed on Background thread

             return params[0].GetAllDevicesState();
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {

         setListAdapter(jsonArray);



        }


    }
}
