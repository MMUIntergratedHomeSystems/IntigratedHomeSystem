package integratedhome.tom.mmu.integratedhomesystem;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        api.getDevicesList();
    }

    public void updateDevicesList(JSONArray devices){
        ListView lv_devices = (ListView) this.findViewById(R.id.lv_devices);

        lv_devices.setAdapter(new ListViewDevicesAdapter(devices,this));
    }
}
