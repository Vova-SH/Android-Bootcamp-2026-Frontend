package ru.sicampus.bootcamp2026.domain.model

/**
 * Статус участника встречи
 */
enum class ParticipantStatus {
    PENDING,    // Ожидание ответа
    CONFIRMED,  // Подтверждено
    DECLINED    // Отклонено
}

