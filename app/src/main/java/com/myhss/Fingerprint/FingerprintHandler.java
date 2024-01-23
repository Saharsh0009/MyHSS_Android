package com.myhss.Fingerprint;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.preference.PreferenceManager;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.myhss.Utils.Functions;
import com.uk.myhss.Main.HomeActivity;
import com.uk.myhss.R;

/**
 * Created by raj.android on 08/22/18.
 */
@RequiresApi(api = Build.VERSION_CODES.P)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {
    private Context context;
    private CancellationSignal cancellationSignal;
    private SharedPreferences sh;
    private SharedPreferences.Editor sh1;
    private String receivedNotiData = "no";
    private String receivedNotiID = "0";

    // Constructor
    FingerprintHandler(Context mContext, String receiNoData, String receinoId) {
        context = mContext;
        receivedNotiData = receiNoData;
        receivedNotiID = receinoId;
    }

    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
        cancellationSignal = new CancellationSignal();
        sh = PreferenceManager.getDefaultSharedPreferences(context);
        sh1 = sh.edit();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    public void stopAuth() {
        if (cancellationSignal != null) {
            cancellationSignal.cancel();
            cancellationSignal = null;
        }
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        this.update("Fingerprint Authentication error\n" + errString);
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        this.update("Fingerprint Authentication help\n" + helpString);
    }

    @Override
    public void onAuthenticationFailed() {
        this.update("Fingerprint Authentication failed.");
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        if (Functions.inBackground == true) {
            Functions.inBackground = false;
        } else {
            Intent i = new Intent(context, HomeActivity.class);
//            i.putExtra("Dashboard", "Dashboard");
            i.putExtra("notification", receivedNotiData);
            i.putExtra("notification_id", receivedNotiID);
            context.startActivity(i);
        }
        ((Activity) context).finish();
    }

    private void update(String e) {
        TextView textView = ((Activity) context).findViewById(R.id.errorText);
        textView.setText(e);
    }
}