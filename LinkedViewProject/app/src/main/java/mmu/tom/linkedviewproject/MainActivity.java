package mmu.tom.linkedviewproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import org.json.JSONArray;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "ShowDevice";


    private ListView GetAllDevicesListView;
    private JSONArray jsonArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG, "  OPened this");


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
