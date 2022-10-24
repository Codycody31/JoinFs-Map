@file:Suppress("DEPRECATION", "unused", "SpellCheckingInspection")

package com.vahngomes.joinfsmap
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.webkit.WebView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

var notificationIdNumerator = 0
@Suppress("SpellCheckingInspection")
class MainActivity : AppCompatActivity() {
    private var mWebview: WebView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startWebView()
        createNotificationChannel()
    }
    override fun onDestroy() {
        super.onDestroy()
    }
    @SuppressLint("SetTextI18n", "SetJavaScriptEnabled")
    private fun startWebView(){
        val text: TextView = findViewById(R.id.android_text)
        val reload: Button = findViewById(R.id.reload_button)
        if (!isNetworkConnected()){
            text.text = "Website failed to port over!"
            text.visibility = View.VISIBLE
            reload.visibility = View.VISIBLE
            val mToast = Toast.makeText(applicationContext,"Map Not Connected!", Toast.LENGTH_SHORT)
            mToast.show()
        }
        else {
            mWebview = WebView(this)
            mWebview!!.settings.javaScriptEnabled = true
            mWebview!!.loadUrl("https://joinfsmap.dotdash.space/")
            setContentView(mWebview)
        }
    }
    private fun isNetworkConnected(): Boolean {
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
    }
    fun reloadPage(@Suppress("UNUSED_PARAMETER") v:View) {
        val ReloadHandler = Handler(Looper.getMainLooper())
        ReloadHandler.post(object : Runnable {
            override fun run() {
                if (!isNetworkConnected()){ }else { finish();startActivity(intent) }
                ReloadHandler.postDelayed(this, 10000)
            }
        })
        if (!isNetworkConnected()){
            val mToast = Toast.makeText(applicationContext,"Please turn on your internet!", Toast.LENGTH_SHORT)
            mToast.show()
        }else {
            finish()
            startActivity(intent)
        }
    }
}
private fun MainActivity.SendNotification(title: String, content: String) {
    val builder = NotificationCompat.Builder(this, "JoinFsMap")
        .setSmallIcon(R.drawable.ic_notification)
        .setContentTitle(title)
        .setContentText(content)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .setOnlyAlertOnce(true)
    with(NotificationManagerCompat.from(this)) {
        // notificationId is a unique int for each notification that you must define
        notificationIdNumerator = notificationIdNumerator + 1
        notify(notificationIdNumerator, builder.build())
    }
}

private fun MainActivity.createNotificationChannel() {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = getString(R.string.channel_name)
        val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("JoinFsMap", name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}