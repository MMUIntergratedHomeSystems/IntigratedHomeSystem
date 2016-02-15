package mmu.tom.linkedviewproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by Tom on 15/02/2016.
 */
public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "ShowDevice";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_main);

        Log.i(TAG, "  OPened this");

        ImageButton button1 = (ImageButton) findViewById(R.id.addNewDeviceButton);

        button1.setOnClickListener(new View.OnClickListener() {
            Class ourClass;

            public void onClick(View v) {

                Intent i;
                i = new Intent(HomeActivity.this, DeviceDetailsActivity.class);
                startActivity(i);
            }
        });

        ImageButton button2 = (ImageButton) findViewById(R.id.viewAllDevicesButton);

        button2.setOnClickListener(new View.OnClickListener() {
            Class ourClass;

            public void onClick(View v) {

                Intent i;
                i = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(i);
            }
        });




    }
}