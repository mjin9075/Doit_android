package com.example.doit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class Svc_receiver extends BroadcastReceiver {

    private static final String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"onReceive() 호출됨");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent intent_A = new Intent(context, Svc_MyService.class);
            context.startForegroundService(intent_A);
        } else {
            Intent intent_A = new Intent(context, Svc_MyService.class);
            context.startService(intent_A);
        }
    }
}
