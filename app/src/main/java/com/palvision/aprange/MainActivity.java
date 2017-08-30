package com.palvision.aprange;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button test;
    TextView displayMessage;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        initUI();
        getWifiMessage();
    }

    public void getWifiMessage() {


        WifiConfiguration wifiConfig = new WifiConfiguration();

        wifiConfig.SSID = String.format("\"%s\"", "PAL");
        wifiConfig.preSharedKey = String.format("\"%s\"", "P@l12345");

        WifiManager wifiManager=(WifiManager)getSystemService(WIFI_SERVICE);
        int netId = wifiManager.addNetwork(wifiConfig);
        wifiManager.disconnect();
        wifiManager.enableNetwork(netId, true);
        wifiManager.reconnect();

        System.out.println(wifiConfig.status);

        System.out.println(wifiManager.getConnectionInfo());

        System.out.println(wifiManager.EXTRA_PREVIOUS_WIFI_STATE);


            // Level of current connection
            int rssi = wifiManager.getConnectionInfo().getRssi();
            int level = WifiManager.calculateSignalLevel(rssi, 5);
            System.out.println("Level is " + level + " out of 5");

            while (level < 4) {

                rssi = wifiManager.getConnectionInfo().getRssi();
                level = WifiManager.calculateSignalLevel(rssi, 5);
                System.out.println("Level is " + level + " out of 5");

                if ((level > 0) && (level <= 1)) {

                 rssi = wifiManager.getConnectionInfo().getRssi();
                level = WifiManager.calculateSignalLevel(rssi, 5);

                Toast.makeText(getApplicationContext(), "This is Level = " + level,
                        Toast.LENGTH_LONG).show();

                displayMessage.setText("Please leave phone on bed");

                break;
            }


            }
    }

    public void initUI() {
        test = (Button) findViewById(R.id.btn_main_test);
        displayMessage = (TextView) findViewById(R.id.tv_main_display);
    }

}
