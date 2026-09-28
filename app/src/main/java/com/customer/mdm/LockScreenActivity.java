package com.customer.mdm;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class LockScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // फुल स्क्रीन मोड (नेविगेशन और स्टेटस बार छिपाने के लिए)
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
              | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
              | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
              | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
              | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
              | View.SYSTEM_UI_FLAG_FULLSCREEN);
              
        setContentView(R.layout.activity_lock_screen);

        TextView tvImei = findViewById(R.id.imeiNumber);
        TextView tvMerchant = findViewById(R.id.merchantDetails);

        tvMerchant.setText("+91-XXXXXXXXXX\nMerchant Details");

        // LockTask Mode शुरू करें (कोई भी बटन काम नहीं करेगा)
        try {
            startLockTask();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // IMEI नंबर निकालना (Device Owner के पास इसकी परमिशन होती है)
        try {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager != null) {
                String imei = telephonyManager.getImei();
                if (imei != null) {
                    tvImei.setText("IMEI No. " + imei);
                }
            }
        } catch (SecurityException e) {
            tvImei.setText("IMEI No. Unavailable");
        }

        findViewById(R.id.btnOfflineCode).setOnClickListener(v -> {
            Toast.makeText(this, "Enter Offline Code dialog will open here", Toast.LENGTH_SHORT).show();
            // यहाँ ऑफलाइन पासवर्ड वेरीफाई होने पर stopLockTask() कॉल करें
        });
    }

    // बैक बटन को पूरी तरह डिसेबल कर दें
    @Override
    public void onBackPressed() {
        // Do nothing
    }
}
