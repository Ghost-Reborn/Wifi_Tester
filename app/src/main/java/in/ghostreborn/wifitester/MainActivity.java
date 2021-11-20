package in.ghostreborn.wifitester;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView mTestText;
    private WifiManager mWifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTestText = findViewById(R.id.temp_text);
        mTestText.setText("Loading...");
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

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
        for (ScanResult wifi : wifiList){
            mTestText.setText(wifi.BSSID);
        }
    }

    private void wifiFail(){
        mTestText.setText("Failed to connect wifi");
    }

}