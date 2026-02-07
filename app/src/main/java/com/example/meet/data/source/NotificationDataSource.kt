package com.example.meet.data.source

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import com.example.meet.data.dto.NotificationDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class NotificationDataSource {

    suspend fun getNotifications(userId: Int): Result<List<NotificationDto>> = withContext(Dispatchers.IO) {
        runCatching {
            val response = Network.client.get("/api/notifications")

            if (response.status != HttpStatusCode.OK) {
                error("Failed to get notifications: ${response.status}")
            }

            response.body<List<NotificationDto>>().filter { it.userId == userId.toLong() }
        }
    }

    suspend fun markAsRead(notificationId: Int): Result<NotificationDto> = withContext(Dispatchers.IO) {
        runCatching {
            // На сервере только CRUD: PUT /api/notifications/{id}
            val current = Network.client.get("/api/notifications/$notificationId")
            if (current.status != HttpStatusCode.OK) {
                error("Failed to get notification: ${current.status}")
            }
            val notification = current.body<NotificationDto>()

            val response = Network.client.put("/api/notifications/$notificationId") {
                setBody(notification.copy(isRead = true))
            }

            if (response.status != HttpStatusCode.OK) {
                error("Failed to mark notification as read: ${response.status}")
            }

            response.body<NotificationDto>()
        }
    }

    suspend fun markAllAsRead(userId: Int): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {

            val response = Network.client.get("/api/notifications")
            if (response.status != HttpStatusCode.OK) {
                error("Failed to get notifications: ${response.status}")
            }
            val items = response.body<List<NotificationDto>>().filter { it.userId == userId.toLong() && !it.isRead }
            items.forEach { n ->
                Network.client.put("/api/notifications/${n.id}") {
                    setBody(n.copy(isRead = true))
                }
            }
        }
    }
}