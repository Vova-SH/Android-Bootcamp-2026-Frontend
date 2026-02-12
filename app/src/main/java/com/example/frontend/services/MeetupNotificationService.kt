package com.example.frontend.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.frontend.MainActivity
import com.example.frontend.R

// Firebase удален из проекта
// Этот сервис можно использовать для локальных уведомлений
class MeetupNotificationService {
    
    companion object {
        private const val CHANNEL_ID = "meetup_invites"
        private const val CHANNEL_NAME = "Приглашения на митапы"
        private const val NOTIFICATION_ID_BASE = 1000
        
        fun showMeetupInvite(
            context: Context,
            organizerName: String,
            meetupDate: String,
            meetupTime: String
        ) {
            val title = "Новое приглашение на митап"
            val content = "$organizerName приглашает вас на митап $meetupDate в $meetupTime"
            showNotification(context, title, content)
        }
        
        fun showInviteResponse(
            context: Context,
            userName: String,
            accepted: Boolean,
            meetupDate: String
        ) {
            val title = "Ответ на приглашение"
            val content = if (accepted) {
                "$userName принял(а) приглашение на митап $meetupDate"
            } else {
                "$userName отклонил(а) приглашение на митап $meetupDate"
            }
            showNotification(context, title, content)
        }
        
        fun showMeetupUpdate(
            context: Context,
            meetupDate: String,
            changeType: String
        ) {
            val title = "Изменение митапа"
            val content = when (changeType) {
                "time_changed" -> "Время митапа $meetupDate было изменено"
                "cancelled" -> "Митап $meetupDate был отменен"
                else -> "Информация о митапе $meetupDate обновлена"
            }
            showNotification(context, title, content)
        }
        
        private fun showNotification(context: Context, title: String, content: String) {
            createNotificationChannel(context)
            
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            
            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            builder.setSmallIcon(R.drawable.ic_launcher_foreground)
            builder.setContentTitle(title)
            builder.setContentText(content)
            builder.setStyle(NotificationCompat.BigTextStyle().bigText(content))
            builder.setPriority(NotificationCompat.PRIORITY_HIGH)
            builder.setAutoCancel(true)
            builder.setContentIntent(pendingIntent)
            
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationId = NOTIFICATION_ID_BASE + System.currentTimeMillis().toInt()
            notificationManager.notify(notificationId, builder.build())
        }
        
        private fun createNotificationChannel(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val importance = NotificationManager.IMPORTANCE_HIGH
                val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
                channel.description = "Уведомления о приглашениях на митапы и их изменениях"
                
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }
    }
}
