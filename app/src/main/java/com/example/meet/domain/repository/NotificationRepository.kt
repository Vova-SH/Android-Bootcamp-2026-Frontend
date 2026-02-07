package com.example.meet.domain.repository

import com.example.meet.domain.entity.Notification

interface NotificationRepository {
    suspend fun getNotifications(userId: Int): Result<List<Notification>>
    suspend fun markAsRead(notificationId: Int): Result<Notification>
    suspend fun markAllAsRead(userId: Int): Result<Unit>
}