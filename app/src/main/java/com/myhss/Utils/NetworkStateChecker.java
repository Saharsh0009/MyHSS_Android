package com.myhss.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

public class NetworkStateChecker extends BroadcastReceiver {

    private Context context;

    private String BASE_LINE_GET_ID = "";
    int pos = 0;
    private boolean iscalledonce = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        boolean isConnected = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);

        if (!isConnected) {
            if (!iscalledonce) {
                iscalledonce = true;

            }
        }
    }

}