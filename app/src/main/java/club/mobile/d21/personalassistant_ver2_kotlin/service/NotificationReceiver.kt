package club.mobile.d21.personalassistant_ver2_kotlin.service

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import club.mobile.d21.personalassistant_ver2_kotlin.R

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            // Trích xuất dữ liệu từ Intent
            val title = intent.getStringExtra("title")
            val description = intent.getStringExtra("description")
            showNotification(context, title, description)
        }
    }

    private fun showNotification(context: Context, title: String?, description: String?) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Sử dụng dữ liệu từ Intent để tạo thông báo
        val notification = NotificationCompat.Builder(context, "channel_id")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title ?: "Default Title")
            .setContentText(description ?: "Default Description")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        // Hiển thị thông báo
        notificationManager.notify(1, notification)
    }
}
