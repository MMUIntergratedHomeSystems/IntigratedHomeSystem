package mmu.tom.linkedviewproject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tom on 08/02/2016.
 */
public class GetAllDeviceListViewAdapter extends BaseAdapter {

    private JSONArray dataArray;
    private Activity activity;
    private String state;

    private static LayoutInflater inflater = null;


    public GetAllDeviceListViewAdapter(JSONArray jsonArray, Activity a){

        this.dataArray = jsonArray;
        this.activity = a;


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
    public View getView(int position, View convertView, ViewGroup parent) {
          // set up the convert view if it's null


        ListCell cell;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.get_all_devices_list_view_cell,null);
            cell = new ListCell();

            cell.deviceName = (TextView) convertView.findViewById(R.id.device_name);
            cell.deviceId = (TextView) convertView.findViewById(R.id.device_id);
            cell.type = (TextView) convertView.findViewById(R.id.type);

            cell.toggleButton = (ToggleButton) convertView.findViewById(R.id.toggleButton);


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
            if(toggle.equals("on")){
                cell.toggleButton.setChecked(true);
            }
            else{
                cell.toggleButton.setChecked(false);
            }


            String device = jsonObject.getString("type");
            if(device.equals("Light")){
                cell.typeImg.setImageResource(R.mipmap.ic_lights_on);
            }
            else if(device.equals("Lock")){
                cell.typeImg.setImageResource(R.mipmap.ic_lock_open_black_24dp);
            }


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

        private ToggleButton toggleButton;

    }


}
