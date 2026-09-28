package com.customer.mdm;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
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
        if (remoteMessage.getData().size() > 0) {
            String command = remoteMessage.getData().get("command");
            if (dpm.isDeviceOwnerApp(getPackageName())) {
                executeAdminCommand(command);
            }
        }
    }

    private void executeAdminCommand(String command) {
        switch (command) {
            case "LOCK_DEVICE":
                Intent lockIntent = new Intent(this, LockScreenActivity.class);
                lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(lockIntent);
                break;

            case "UNLOCK_DEVICE":
                // भविष्य में अनलॉक लॉजिक के लिए
                break;

            case "DISABLE_CAMERA":
                dpm.setCameraDisabled(adminComponent, true);
                break;

            case "RELEASE_DEVICE":
                // कस्टमर की EMI पूरी होने पर एडमिन पावर छोड़ना
                try {
                    dpm.clearDeviceOwnerApp(getPackageName());
                    // पावर छोड़ते ही खुद को अनइंस्टॉल करने का प्रॉम्प्ट देना
                    Intent intent = new Intent(Intent.ACTION_DELETE);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (SecurityException e) {
                    Log.e("MDM", "Release failed", e);
                }
                break;
        }
    }
    
    @Override
    public void onNewToken(String token) {
        Log.d("FCM_TOKEN", "New Token: " + token);
    }
}
