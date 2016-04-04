package integratedhome.tom.mmu.integratedhomesystem;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tom on 04/04/2016.
 */
public class ListViewDevicesAdapter extends BaseAdapter {

    private JSONArray dataArray;
    private Activity activity;
    private String state;

    private static LayoutInflater inflater = null;
    private static final String TAG = "FormatDevice";


    public ListViewDevicesAdapter(JSONArray jsonArray,Activity a){
        this.activity = a;
        this.dataArray = jsonArray;
        inflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public int getCount() {
        return this.dataArray.length();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // set up the convert view if it's null
        ListCell cell;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.devices_list_view_cell,null);
            cell = new ListCell();

            cell.deviceName = (TextView) convertView.findViewById(R.id.device_name);
            cell.deviceId = (TextView) convertView.findViewById(R.id.device_id);
            cell.type = (TextView) convertView.findViewById(R.id.type);

            cell.toggleSwitch = (Switch) convertView.findViewById(R.id.device_status);

            cell.typeImg = (ImageView) convertView.findViewById(R.id.device_type);



            convertView.setTag(cell);

        }
        else{
            cell=(ListCell) convertView.getTag();
        }

        // changes the cell data here

        try{
            JSONObject jsonObject = this.dataArray.getJSONObject(position);
            cell.deviceName.setText(jsonObject.getString("name"));
            cell.deviceId.setText(" " + jsonObject.getString("deviceID"));
            cell.type.setText(" " + jsonObject.getString("type"));


            String toggle = jsonObject.getString("currentState");
            if(toggle.equalsIgnoreCase("on")){
                cell.toggleSwitch.setChecked(true);

                Log.i(TAG, "  TEST on");

            }
            else if (toggle.equalsIgnoreCase("off")){
                cell.toggleSwitch.setChecked(false);
                Log.i(TAG, "  TEST off");
            }


            cell.toggleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(isChecked)
                    {

                        Log.i(TAG, "  THIS HAS NOT BEEN TOGGLE ON");
                    }
                    else
                    {
                        //your action
                        Log.i(TAG, "   THIS HAS NOT BEEN TOGGLE OFF");
                    }
                }
            });


//            String device = jsonObject.getString("type");
//            if(device.equalsIgnoreCase("Light")){
//                cell.typeImg.setImageResource(R.mipmap.ic_lights_on);
//            }
//            else if(device.equalsIgnoreCase("Lock")){
//                cell.typeImg.setImageResource(R.mipmap.ic_lock_open_black_24dp);
//            }
            // remember to set the image to type in future


        }
        catch(JSONException e){
            e.printStackTrace();
        }



        return convertView;
    }



    private class ListCell{

        private TextView deviceName;
        private TextView deviceId;
        private TextView type;

        private ImageView typeImg;

        private Switch toggleSwitch;

    }
}

