package integratedhome.tom.mmu.integratedhomesystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
    private IHS_Api api;

    private static LayoutInflater inflater = null;
    private static final String TAG = "FormatDevice";


    public ListViewDevicesAdapter(JSONArray jsonArray, Activity a, IHS_Api api) {
        this.activity = a;
        this.api = api;
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
        try {
            final JSONObject jsonObject = this.dataArray.getJSONObject(position);
            cell = new ListCell();
            String type = jsonObject.getString("type");
            if (type.equalsIgnoreCase("thermostat")) {
                convertView = inflater.inflate(R.layout.devices_list_view_cell_thermo, null);
                cell.temperature = (TextView) convertView.findViewById(R.id.tv_temperature);
            }
            else{
                convertView = inflater.inflate(R.layout.devices_list_view_cell_toggle, null);
                cell.toggleSwitch = (Switch) convertView.findViewById(R.id.device_status);
            }

            cell.deviceName = (TextView) convertView.findViewById(R.id.device_name);
            cell.deviceId = (TextView) convertView.findViewById(R.id.device_id);
            cell.type = (TextView) convertView.findViewById(R.id.type);
            cell.mainLayout = (LinearLayout) convertView.findViewById(R.id.ll_cell_layout);
            cell.typeImg = (ImageView) convertView.findViewById(R.id.device_type);

            convertView.setTag(cell);

            final String deviceID = jsonObject.getString("deviceID");
            cell.deviceName.setText(jsonObject.getString("name"));
            cell.deviceId.setText(deviceID);
            cell.type.setText(jsonObject.getString("type"));

            String state = jsonObject.getString("currentState");

            if(cell.toggleSwitch != null) {
                if (state.equalsIgnoreCase("1")) {
                    cell.toggleSwitch.setChecked(true);
                } else if (state.equalsIgnoreCase("0")) {
                    cell.toggleSwitch.setChecked(false);
                }
            }
            if(cell.temperature != null){
                cell.temperature.setText(state+"°");
            }

            if(cell.toggleSwitch != null) {
                cell.toggleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if (isChecked) {
                            if (buttonView.isShown()) {
                                Log.v(TAG, "Turned on " + deviceID);
                                api.setState(deviceID, true);
                            }
                        } else {
                            if (buttonView.isShown()) {
                                Log.v(TAG, "Turned off " + deviceID);
                                api.setState(deviceID, false);
                            }
                        }
                    }
                });
            }

            cell.mainLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    api.removeDevice(deviceID);
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("Are you sure you want to delete this device?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                    return false;
                }
            });

            String device = jsonObject.getString("type");
            int typeimage;
            switch (device) {
                case "Light": {
                    typeimage = R.drawable.ic_lightbulb_outline_black_24dp;
                    break;
                }
                case "Lock": {
                    typeimage = R.drawable.ic_lock_open_black_24dp;
                    break;
                }
                default: {
                    typeimage = R.drawable.ic_home_black_24dp;
                    break;
                }
            }
            cell.typeImg.setImageResource(typeimage);
        } catch (JSONException e) {
            e.printStackTrace();
    }


        return convertView;
    }


    private class ListCell {

        private TextView deviceName;
        private TextView deviceId;
        private TextView type;

        private ImageView typeImg;
        private LinearLayout mainLayout;

        private Switch toggleSwitch;

        private TextView temperature;

    }
}

