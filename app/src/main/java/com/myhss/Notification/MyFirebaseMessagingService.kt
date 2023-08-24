package com.myhss.Notification

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
import com.myhss.appConstants.AppParam
import java.io.InputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL


class MyFirebaseMessagingService : FirebaseMessagingService() {

    val TAG = String::class.java.simpleName
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var sessionManager: SessionManager

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        DebugLog.d("From : ${remoteMessage.from}")
        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            DebugLog.d("Message data payload : ${remoteMessage.data}")
            // Compose and show notification
            if (!remoteMessage.data.isNullOrEmpty()) {
                if (remoteMessage.data["type"] != null) {
                    AppParam.NOTIFIC_VALUE = remoteMessage.data["type"].toString()
                } else {
                    AppParam.NOTIFIC_VALUE = "0"
                }
                val title: String = remoteMessage.data["suchana_title"].toString()
                val msg: String = remoteMessage.data["message"].toString()
                DebugLog.e("Stype : ${AppParam.NOTIFIC_VALUE}")
                sendNotification(title, msg)
            }
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            DebugLog.d("Message Notification Body : ${it.body}")
            sendNotification(
                remoteMessage.notification?.title!!,
                remoteMessage.notification?.body!!
            )
        }
    }

    override fun onNewToken(token: String) {
        sessionManager = SessionManager(this)
        sharedPreferences = getSharedPreferences("production", Context.MODE_PRIVATE)

        DebugLog.d("Refreshed token : $token")
        sessionManager.saveFCMDEVICE_TOKEN(token)

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String?) {
        DebugLog.d("sendRegistrationTokenToServer($token)")
    }

    private fun sendNotification(title: String, messageBody: String) {
        val notID = getNotificationId()
        val intent = Intent(this, SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra(AppParam.NOTIFIC_KEY, AppParam.NOTIFIC_VALUE)
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
            notID /* ID of notification */,
            notificationBuilder.build()
        )
    }

    private fun getNotificationId(): Int {
        val rnd = Random()
        return rnd.nextInt(9000) + 100
    }
}