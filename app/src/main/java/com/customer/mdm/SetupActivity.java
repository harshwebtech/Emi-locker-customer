package com.customer.mdm;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SetupActivity extends Activity {

    private DevicePolicyManager dpm;
    private ComponentName componentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(this, MyAdminReceiver.class);

        TextView tvStatus = findViewById(R.id.tvStatus);
        Button btnConnect = findViewById(R.id.btnConnectAdmin);

        if (dpm.isDeviceOwnerApp(getPackageName())) {
            tvStatus.setText("Ownership Granted. Ready to Connect.");
            // LockTask मोड के लिए इस ऐप को वाइटलिस्ट करें ताकि लॉक स्क्रीन से कोई बाहर न जा सके
            dpm.setLockTaskPackages(componentName, new String[]{getPackageName()});
        } else {
            tvStatus.setText("Waiting for Wireless ADB Setup...");
            btnConnect.setEnabled(false);
        }

        btnConnect.setOnClickListener(v -> {
            Toast.makeText(this, "Connected to Admin Dashboard!", Toast.LENGTH_SHORT).show();
            // डेमो के लिए सीधा लॉक स्क्रीन ओपन कर रहे हैं
            startActivity(new Intent(SetupActivity.this, LockScreenActivity.class));
            finish();
        });
    }
}
