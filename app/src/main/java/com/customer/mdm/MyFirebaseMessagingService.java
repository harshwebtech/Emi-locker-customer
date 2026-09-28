package com.customer.mdm;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private DevicePolicyManager dpm;
    private ComponentName adminComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        adminComponent = new ComponentName(this, MyAdminReceiver.class);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // यह फंक्शन तब ट्रिगर होगा जब डीलर ऐप कोई कमांड भेजेगा
        if (remoteMessage.getData().size() > 0) {
            String command = remoteMessage.getData().get("command");
            
            if (dpm.isDeviceOwnerApp(getPackageName())) {
                executeAdminCommand(command);
            } else {
                Log.e("MDM", "App is not device owner yet!");
            }
        }
    }

    private void executeAdminCommand(String command) {
        switch (command) {
            case "LOCK_DEVICE":
                // फोन को लॉक स्क्रीन पर ले जाने का कमांड
                Intent lockIntent = new Intent(this, LockScreenActivity.class);
                lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(lockIntent);
                break;

            case "UNLOCK_DEVICE":
                // अगर फोन लॉक है, तो अनलॉक करने के लिए ब्रॉडकास्ट भेजें
                Intent unlockIntent = new Intent("com.customer.mdm.ACTION_UNLOCK");
                sendBroadcast(unlockIntent);
                break;

            case "DISABLE_CAMERA":
                dpm.setCameraDisabled(adminComponent, true);
                break;

            case "ENABLE_CAMERA":
                dpm.setCameraDisabled(adminComponent, false);
                break;

            case "WIPE_DATA":
                // फैक्ट्री रीसेट कमांड
                dpm.wipeData(0);
                break;
        }
    }

    @Override
    public void onNewToken(String token) {
        // यह FCM टोकन आपको अपने सर्वर (या एडमिन पैनल) पर भेजना होगा
        Log.d("FCM_TOKEN", "New Token: " + token);
        // TODO: Send this token to your database
    }
}
