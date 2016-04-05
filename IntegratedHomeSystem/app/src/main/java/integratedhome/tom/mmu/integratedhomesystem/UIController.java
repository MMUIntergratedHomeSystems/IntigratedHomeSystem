package integratedhome.tom.mmu.integratedhomesystem;

import android.widget.Toast;
import org.json.JSONArray;

public class UIController {

    MainActivity mainActivity;

    public UIController(MainActivity ma) {
        this.mainActivity = ma;
    }

    public void updateDevicesList(JSONArray devices) {
        mainActivity.updateDevicesList(devices);
    }

    public void makeError(String message) {
        Toast.makeText(mainActivity.getApplicationContext(), "ERROR: " + message, Toast.LENGTH_LONG).show();
    }

}
