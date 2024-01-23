package com.myhss.Notification

import android.app.ActivityManager
import android.app.Notification.DEFAULT_VIBRATE
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.uk.myhss.R
import com.uk.myhss.Splash.SplashActivity
import com.uk.myhss.Utils.SessionManager
import java.util.*
import android.graphics.Bitmap
import com.myhss.Utils.DebugLog
import com.myhss.Utils.UtilCommon
import com.myhss.appConstants.AppParam
import com.myhss.ui.SuchanaBoard.NotificationList
import com.myhss.ui.SuchanaBoard.SuchanaBoardActivity
import java.io.InputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL


class MyFirebaseMessagingService : FirebaseMessagingService() {

    val TAG = String::class.java.simpleName
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var sessionManager: SessionManager

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        var notification_type = "0"
        var notifc_id = "0"
        if (remoteMessage.data.isNotEmpty()) {
            if (!remoteMessage.data.isNullOrEmpty()) {
                DebugLog.e("remoteMessage.data => ${remoteMessage.data}")
                if (remoteMessage.data["type"] != null) {
                    notification_type = remoteMessage.data["type"].toString()
                } else {
                    notification_type = "0"
                }
                notifc_id = remoteMessage.data["suchana_id"].toString()
                val title: String = remoteMessage.data["suchana_title"].toString()
                val msg: String = remoteMessage.data["message"].toString()
                sendNotification(title, msg, notification_type, notifc_id)
            }
        }

        remoteMessage.notification?.let {
            sendNotification(
                remoteMessage.notification?.title!!,
                remoteMessage.notification?.body!!,
                notification_type,
                notifc_id
            )
        }
    }

    override fun onNewToken(token: String) {
        sessionManager = SessionManager(this)
        sharedPreferences = getSharedPreferences("production", Context.MODE_PRIVATE)
        sessionManager.saveFCMDEVICE_TOKEN(token)
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String?) {
        DebugLog.d("sendRegistrationTokenToServer($token)")
    }

    private fun sendNotification(
        title: String,
        messageBody: String,
        Stype: String,
        not_id: String
    ) {

        val packageName = "com.uk.myhss"
        val isAppInForeground = UtilCommon.isAppInForeground(this, packageName)
        val isAppRunning = UtilCommon.isAppRunning(this, packageName)

        val intent: Intent

        if (isAppInForeground) {
            DebugLog.e("App is in isAppInForeground")
            if (Stype == "0") {
                intent = Intent(this, SuchanaBoardActivity::class.java)
            } else {
                intent = Intent(this, NotificationList::class.java)
            }
        } else if (isAppRunning) {
            DebugLog.e("App is in isAppRunning")
            intent = Intent(this, SplashActivity::class.java)
        } else {
            DebugLog.e("App is in Not running")
            intent = Intent(this, SplashActivity::class.java)
        }

        val notID = getNotificationId()
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(AppParam.NOTIFIC_KEY, Stype)
        intent.putExtra(AppParam.NOTIFIC_ID, not_id)
        val pendingIntent = PendingIntent.getActivity(
            this, notID /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        // code change test
        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notif)
            .setContentTitle(title)  // getString(R.string.app_name)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.app_logo))
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setDefaults(DEFAULT_VIBRATE)
//                .setStyle(NotificationCompat.BigPictureStyle().
//                 bigPicture(BitmapFactory.decodeResource(resources, R.drawable.splash)))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(
            notID,
            notificationBuilder.build()
        )

//        notificationManager.cancelAll()
    }

    private fun getNotificationId(): Int {
        val rnd = Random()
        return rnd.nextInt(9000) + 100
    }
}