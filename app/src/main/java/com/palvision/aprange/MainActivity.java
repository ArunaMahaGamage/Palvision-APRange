package com.palvision.aprange;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button test;
    TextView displayMessage, displaySSID, displayLevel, httpResponse;
    Context context;

    Boolean b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        final Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                b = isOnline();
                System.out.println(b);

                initUI();

                handler.postDelayed(this, 1000);
            }
        };

        handler.postDelayed(r, 1000);

    }
    public void initUI() {
        test = (Button) findViewById(R.id.btn_main_test);
        displayMessage = (TextView) findViewById(R.id.tv_main_display);
        displaySSID = (TextView) findViewById(R.id.tv_main_ssid);
        displayLevel = (TextView) findViewById(R.id.tv_main_level);
        httpResponse = (TextView) findViewById(R.id.tv_main_httprespone);


        showSSID();
        testRequest();

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWifiMessage();
            }
        });
    }

    public void showSSID() {
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


        displaySSID.setText(wifiConfig.SSID);
        displayLevel.setText(new String(String.valueOf(level)));
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

                if ((level > 0) && (level <= 2)) {

                 rssi = wifiManager.getConnectionInfo().getRssi();
                level = WifiManager.calculateSignalLevel(rssi, 5);

                Toast.makeText(getApplicationContext(), "This is Level = " + level,
                        Toast.LENGTH_LONG).show();

                    Toast.makeText(getApplicationContext(), "This is Level = " + b,
                            Toast.LENGTH_LONG).show();

                    displayMessage.setText("Please leave phone on bed");
                    displaySSID.setText(wifiConfig.SSID);
                    displayLevel.setText(new String(String.valueOf(level)));

                break;
            }


            }
    }



    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public void testRequest() {
        String url = "http://my-json-feed";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        httpResponse.setText("Response: " + response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        httpResponse.setText("Response: " + error.toString());

                    }
                });

// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }

}
