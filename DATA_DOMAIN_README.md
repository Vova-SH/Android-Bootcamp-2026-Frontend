# Информация о Data и Domain слоях

## ✅ Что реализовано

### Domain Layer (Бизнес-логика)
- ✅ **Модели данных**: User, Meeting, Invitation, AuthTokens, FreeTimeSlot
- ✅ **Enum классы**: MeetingStatus, ParticipantStatus
- ✅ **Repository интерфейсы**: AuthRepository, ProfileRepository, MeetingRepository, InvitationRepository
- ✅ **Use Cases**: Login, Register, Logout, CreateMeeting, GetMeetings, GetInvitations, RespondToInvitation, GetProfile
- ✅ **Result wrapper**: Для обработки успеха/ошибки/загрузки

### Data Layer (Работа с данными)
- ✅ **Retrofit API интерфейсы**: AuthApi, ProfileApi, MeetingApi, InvitationApi
- ✅ **DTO модели**: Все необходимые Request/Response модели с @Serializable
- ✅ **Mappers**: Преобразование DTO → Domain моделей
- ✅ **Repository реализации**: Все 4 репозитория полностью реализованы
- ✅ **TokenDataStore**: Хранение JWT токенов в DataStore
- ✅ **AuthInterceptor**: Автоматическое добавление Bearer токенов к запросам

### Dependency Injection (Hilt)
- ✅ **NetworkModule**: Retrofit, OkHttp, API интерфейсы, DataStore
- ✅ **RepositoryModule**: Связывание интерфейсов с реализациями
- ✅ **Application класс**: @HiltAndroidApp
- ✅ **MainActivity**: @AndroidEntryPoint

### Конфигурация
- ✅ **Gradle зависимости**: Retrofit, OkHttp, Kotlin Serialization, Hilt, DataStore, Coroutines
- ✅ **AndroidManifest**: INTERNET permission, cleartext traffic для localhost
- ✅ **Плагины**: KSP, Hilt, Serialization

## 📋 Полная информация для написания приложения

### 1. API Эндпоинты

#### Авторизация (без токена)
```
POST /api/v1/auth/register - Регистрация
POST /api/v1/auth/login - Вход
POST /api/v1/auth/refresh - Обновление токенов (нужен refresh token)
POST /api/v1/auth/logout - Выход
```

#### Профиль (требует Bearer token)
```
GET /api/v1/profile - Получить свой профиль
PUT /api/v1/profile - Обновить профиль
PUT /api/v1/profile/avatar - Обновить аватар
PUT /api/v1/profile/reset-password - Сменить пароль
GET /api/v1/profile/public/all?page=0&size=20 - Список всех пользователей (публичный)
```

#### Встречи (требует Bearer token)
```
GET /api/v1/meetings?status=SCHEDULED&page=0&size=20 - Список встреч
POST /api/v1/meetings - Создать встречу
GET /api/v1/meetings/{meetingId} - Получить встречу
PUT /api/v1/meetings/{meetingId}/cancel - Отменить встречу
DELETE /api/v1/meetings/{meetingId} - Удалить встречу
POST /api/v1/meetings/freeTime - Получить свободные слоты
```

#### Приглашения (требует Bearer token)
```
GET /api/v1/invitations - Список приглашений
GET /api/v1/invitations/{meetingId} - Детали приглашения
PUT /api/v1/invitations/{meetingId}/respond - Ответить на приглашение
```

### 2. JWT Авторизация

**Access Token** (для всех защищенных эндпоинтов):
```
Authorization: Bearer <ACCESS_TOKEN>
```

**Refresh Token** (только для `/auth/refresh`):
```
Authorization: Bearer <REFRESH_TOKEN>
```

**Автоматическое добавление токенов**: `AuthInterceptor` делает это автоматически!

### 3. Модели данных

#### AuthTokens
```kotlin
data class AuthTokens(
    val userId: UUID,
    val username: String,
    val email: String,
    val accessToken: String,
    val refreshToken: String,
    val accessTokenExpiresAt: LocalDateTime,
    val refreshTokenExpiresAt: LocalDateTime
)
```

#### User
```kotlin
data class User(
    val id: UUID,
    val username: String,
    val email: String,
    val avatarUrl: String?
)
```

#### Meeting
```kotlin
data class Meeting(
    val id: UUID,
    val organizerId: UUID,
    val organizerUsername: String,
    val title: String,
    val description: String?,
    val location: String?,
    val startTime: LocalDateTime, // Только ровные часы!
    val endTime: LocalDateTime,
    val status: MeetingStatus, // SCHEDULED, CANCELLED, COMPLETED
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val participants: List<Participant>
)
```

#### Invitation
```kotlin
data class Invitation(
    val id: UUID,
    val meetingId: UUID,
    val meetingTitle: String,
    val meetingDescription: String?,
    val meetingLocation: String?,
    val meetingStartTime: LocalDateTime,
    val meetingEndTime: LocalDateTime,
    val organizerUsername: String,
    val status: ParticipantStatus, // PENDING, CONFIRMED, DECLINED
    val createdAt: LocalDateTime
)
```

### 4. Use Cases (готовые к использованию)

```kotlin
// Инъекция через Hilt
@HiltViewModel
class YourViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val createMeetingUseCase: CreateMeetingUseCase,
    private val getUserMeetingsUseCase: GetUserMeetingsUseCase,
    private val getInvitationsUseCase: GetInvitationsUseCase,
    private val respondToInvitationUseCase: RespondToInvitationUseCase,
    private val getProfileUseCase: GetProfileUseCase
) : ViewModel()
```

### 5. Пример использования

```kotlin
viewModelScope.launch {
    when (val result = loginUseCase(email, password)) {
        is Result.Success -> {
            val authTokens = result.data
            // Токены автоматически сохранены!
            // Переход на главный экран
        }
        is Result.Error -> {
            val error = result.exception.message
            // Показать ошибку
        }
        is Result.Loading -> {
            // Показать загрузку
        }
    }
}
```

### 6. Валидация

**Автоматическая валидация в Use Cases**:
- ✅ Email формат
- ✅ Минимальная длина пароля (6 символов)
- ✅ Минимальная длина username (2 символа)
- ✅ Время начала встречи должно быть ровным часом (9:00, 10:00, и т.д.)
- ✅ Время окончания после времени начала
- ✅ Встреча не в прошлом
- ✅ Минимум 1 участник

### 7. Конфигурация сервера

**По умолчанию**: `http://localhost:8080/`

**Изменить**: В файле `NetworkModule.kt` измените константу:
```kotlin
private const val BASE_URL = "http://your-server.com/"
```

### 8. Важные особенности

#### Временные слоты
- Встречи могут начинаться **только ровно в начале часа** (9:00, 10:00, 11:00)
- Это требование ТЗ и валидируется в `CreateMeetingUseCase`

#### Пагинация
- По умолчанию: `page=0, size=20`
- Spring Page API (совместимо с бэкендом)

#### Refresh flow
- Refresh токен уже поддерживается
- При 401 ошибке нужно будет вызвать `authRepository.refresh()`
- TODO: Добавить автоматический refresh при 401 (можно сделать через Authenticator в OkHttp)

### 9. Структура пакетов

```
ru.sicampus.bootcamp2026/
├── data/
│   ├── local/
│   │   └── TokenDataStore.kt
│   ├── mapper/
│   │   ├── AuthMapper.kt
│   │   ├── ProfileMapper.kt
│   │   ├── MeetingMapper.kt
│   │   └── InvitationMapper.kt
│   ├── remote/
│   │   ├── api/
│   │   │   ├── AuthApi.kt
│   │   │   ├── ProfileApi.kt
│   │   │   ├── MeetingApi.kt
│   │   │   └── InvitationApi.kt
│   │   ├── dto/
│   │   │   ├── AuthDto.kt
│   │   │   ├── ProfileDto.kt
│   │   │   ├── MeetingDto.kt
│   │   │   ├── InvitationDto.kt
│   │   │   └── PageResponse.kt
│   │   └── interceptor/
│   │       └── AuthInterceptor.kt
│   └── repository/
│       ├── AuthRepositoryImpl.kt
│       ├── ProfileRepositoryImpl.kt
│       ├── MeetingRepositoryImpl.kt
│       └── InvitationRepositoryImpl.kt
├── domain/
│   ├── model/
│   │   ├── AuthTokens.kt
│   │   ├── User.kt
│   │   ├── Meeting.kt
│   │   ├── Invitation.kt
│   │   ├── MeetingStatus.kt
│   │   ├── ParticipantStatus.kt
│   │   ├── FreeTimeSlot.kt
│   │   └── PaginatedData.kt
│   ├── repository/
│   │   ├── AuthRepository.kt
│   │   ├── ProfileRepository.kt
│   │   ├── MeetingRepository.kt
│   │   └── InvitationRepository.kt
│   ├── usecase/
│   │   ├── auth/
│   │   │   ├── LoginUseCase.kt
│   │   │   ├── RegisterUseCase.kt
│   │   │   └── LogoutUseCase.kt
│   │   ├── meeting/
│   │   │   ├── CreateMeetingUseCase.kt
│   │   │   └── GetUserMeetingsUseCase.kt
│   │   ├── invitation/
│   │   │   ├── GetInvitationsUseCase.kt
│   │   │   └── RespondToInvitationUseCase.kt
│   │   └── profile/
│   │       └── GetProfileUseCase.kt
│   └── util/
│       └── Result.kt
├── di/
│   ├── NetworkModule.kt
│   └── RepositoryModule.kt
└── MeetingPlannerApplication.kt
```

### 10. Что нужно сделать дальше

1. **Создать ViewModels** для каждого экрана с использованием готовых Use Cases
2. **Реализовать UI** с Jetpack Compose
3. **Настроить Navigation** между экранами
4. **Добавить автоматический refresh** токенов при 401 (OkHttp Authenticator)
5. **Добавить обработку ошибок** в UI (Toast/Snackbar)

### 11. Технологии

- ✅ **Retrofit 2.11.0** - REST API клиент
- ✅ **OkHttp 4.12.0** - HTTP клиент с логированием
- ✅ **Kotlin Serialization 1.7.3** - JSON сериализация
- ✅ **Hilt 2.52** - Dependency Injection
- ✅ **DataStore 1.1.1** - Хранение токенов
- ✅ **Coroutines 1.8.1** - Асинхронность

## 📚 Полная документация

Подробная документация в файле: **DATA_DOMAIN_LAYER.md**

## 🚀 Быстрый старт

1. Запустите бэкенд на `localhost:8080`
2. Соберите проект: `./gradlew build`
3. Используйте готовые Use Cases в ViewModels
4. Все токены и авторизация работают автоматически!

