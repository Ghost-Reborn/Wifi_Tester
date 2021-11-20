package in.ghostreborn.wifitester;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private WifiManager mWifiManager;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        listView = findViewById(R.id.wifi_list_view);

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean success = intent.getBooleanExtra(
                        WifiManager.EXTRA_RESULTS_UPDATED, false
                );

                if (success)
                    wifiSuccess();
                else
                    wifiFail();
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(receiver, filter);

        boolean success = mWifiManager.startScan();
        if (!success)
            wifiFail();

    }

    private void wifiSuccess(){
        List<ScanResult> wifiList = mWifiManager.getScanResults();
        ArrayList<String> wifis = new ArrayList<>();
        for (ScanResult wifi: wifiList){
            wifis.add(wifi.BSSID);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                MainActivity.this,
                android.R.layout.simple_list_item_1,
                wifis
        );
        listView.setAdapter(adapter);
    }

    private void wifiFail(){
        Toast.makeText(MainActivity.this, "Failed to connect to wifi", Toast.LENGTH_SHORT).show();
    }

}