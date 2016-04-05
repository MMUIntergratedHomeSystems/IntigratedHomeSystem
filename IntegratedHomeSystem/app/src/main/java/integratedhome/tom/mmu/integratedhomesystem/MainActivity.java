package integratedhome.tom.mmu.integratedhomesystem;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import org.json.JSONArray;

public class MainActivity extends AppCompatActivity {

    private IHS_Api api;
    private UIController uiController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        uiController = new UIController(this);
        api = new IHS_Api(uiController);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final Dialog dialog = new Dialog(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.setContentView(R.layout.new_device);
                dialog.setTitle("Custom Alert Dialog");

                Button btn_cancel = (Button) dialog.findViewById(R.id.btn_new_cancel);
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                Button btn_add = (Button) dialog.findViewById(R.id.btn_new_submit);
                btn_add.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        EditText et_devicename = (EditText)dialog.findViewById(R.id.edit_name);
                        EditText et_devicemanufacturer = (EditText)dialog.findViewById(R.id.edit_manufacturer);
                        EditText et_devicelocation = (EditText)dialog.findViewById(R.id.edit_location);
                        EditText et_devicetype = (EditText)dialog.findViewById(R.id.edit_type);
                        EditText et_houseid = (EditText) dialog.findViewById(R.id.edit_houseid);
                        EditText et_deviceid = (EditText)dialog.findViewById(R.id.edit_device_id);

                        String device_name = et_devicename.getText().toString();
                        String device_manufacturer = et_devicemanufacturer.getText().toString();
                        String device_location = et_devicelocation.getText().toString();
                        String device_type = et_devicetype.getText().toString();
                        String house_id = et_houseid.getText().toString();
                        String device_id = et_deviceid.getText().toString();

                        api.addDevice(device_name,device_manufacturer,device_location,device_type,device_id,house_id);
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });

        api.getDevicesList();
    }

    public void updateDevicesList(JSONArray devices){
        ListView lv_devices = (ListView) this.findViewById(R.id.lv_devices);
        lv_devices.setAdapter(new ListViewDevicesAdapter(devices,this,api));
    }
}
