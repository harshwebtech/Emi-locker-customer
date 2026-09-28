package com.customer.mdm;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyAdminReceiver extends DeviceAdminReceiver {
    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
        Toast.makeText(context, "Device Owner Access Granted!", Toast.LENGTH_LONG).show();
        
        // ओनरशिप मिलते ही कस्टमर ऐप को अपने आप स्क्रीन पर खोलने का कोड
        Intent launchIntent = new Intent(context, SetupActivity.class);
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(launchIntent);
    }
}
