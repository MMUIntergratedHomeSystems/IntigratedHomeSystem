package mmu.tom.linkedviewproject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import org.json.JSONArray;

public class MainActivity extends AppCompatActivity {

    private ListView GetAllDevicesListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.GetAllDevicesListView = (ListView) this.findViewById(R.id.GetAllDevicesListView);

        new GetAllDevicesTask().execute(new ApiConnector());


    }

    public void setListAdapter(JSONArray jsonArray){

        this.GetAllDevicesListView.setAdapter((new GetAllDeviceListViewAdapter(jsonArray,this)));

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
