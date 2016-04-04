package integratedhome.tom.mmu.integratedhomesystem;

import org.json.JSONArray;

public class UIController {

    MainActivity mainActivity;

    public UIController(MainActivity ma){
        this.mainActivity = ma;
    }

    public void updateDevicesList(JSONArray devices){
        mainActivity.updateDevicesList(devices);
    }
}
