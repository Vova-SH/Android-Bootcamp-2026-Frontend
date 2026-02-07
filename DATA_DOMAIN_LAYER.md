# Data и Domain слои - Документация

## Обзор архитектуры

Приложение следует Clean Architecture с разделением на слои:
- **Domain** - бизнес-логика и модели данных
- **Data** - работа с данными (API, DataStore)
- **Presentation** - UI (будет реализован отдельно)

## Domain слой

### Модели данных (`domain/model/`)

#### AuthTokens
- `userId: UUID` - ID пользователя
- `username: String` - Имя пользователя
- `email: String` - Email
- `accessToken: String` - Access токен для API
- `refreshToken: String` - Refresh токен
- `accessTokenExpiresAt: LocalDateTime` - Время истечения access токена
- `refreshTokenExpiresAt: LocalDateTime` - Время истечения refresh токена

#### User
- `id: UUID` - ID пользователя
- `username: String` - Имя пользователя
- `email: String` - Email
- `avatarUrl: String?` - URL аватара (опционально)

#### Meeting
- `id: UUID` - ID встречи
- `organizerId: UUID` - ID организатора
- `organizerUsername: String` - Имя организатора
- `title: String` - Название встречи
- `description: String?` - Описание (опционально)
- `location: String?` - Место проведения (опционально)
- `startTime: LocalDateTime` - Время начала (должно быть ровно в начале часа)
- `endTime: LocalDateTime` - Время окончания
- `status: MeetingStatus` - Статус (SCHEDULED, CANCELLED, COMPLETED)
- `createdAt: LocalDateTime` - Время создания
- `updatedAt: LocalDateTime` - Время обновления
- `participants: List<Participant>` - Список участников

#### Invitation
- `id: UUID` - ID приглашения
- `meetingId: UUID` - ID встречи
- `meetingTitle: String` - Название встречи
- `meetingDescription: String?` - Описание встречи
- `meetingLocation: String?` - Место проведения
- `meetingStartTime: LocalDateTime` - Время начала
- `meetingEndTime: LocalDateTime` - Время окончания
- `organizerUsername: String` - Имя организатора
- `status: ParticipantStatus` - Статус (PENDING, CONFIRMED, DECLINED)
- `createdAt: LocalDateTime` - Время создания

### Репозитории (интерфейсы в `domain/repository/`)

#### AuthRepository
- `register()` - Регистрация нового пользователя
- `login()` - Вход в систему
- `refresh()` - Обновление токенов
- `logout()` - Выход из системы
- `getAccessToken()` - Получение access токена
- `getRefreshToken()` - Получение refresh токена
- `saveTokens()` - Сохранение токенов
- `clearTokens()` - Очистка токенов
- `isAuthenticated()` - Проверка авторизации

#### ProfileRepository
- `getProfile()` - Получение профиля текущего пользователя
- `updateProfile()` - Обновление профиля
- `updateAvatar()` - Обновление аватара
- `resetPassword()` - Сброс пароля
- `getAllUsers()` - Получение списка всех пользователей (с пагинацией)
- `getCachedProfile()` - Получение кэшированного профиля

#### MeetingRepository
- `createMeeting()` - Создание новой встречи
- `getMeetingById()` - Получение встречи по ID
- `getUserMeetings()` - Получение списка встреч пользователя (с фильтрацией и пагинацией)
- `cancelMeeting()` - Отмена встречи
- `deleteMeeting()` - Удаление встречи
- `getFreeTime()` - Получение свободных временных слотов для участников

#### InvitationRepository
- `getInvitations()` - Получение списка приглашений
- `getInvitationDetails()` - Получение деталей приглашения
- `respondToInvitation()` - Ответ на приглашение (принять/отклонить)

### Use Cases (`domain/usecase/`)

#### Auth
- `RegisterUseCase` - Регистрация с валидацией
- `LoginUseCase` - Вход с валидацией
- `LogoutUseCase` - Выход

#### Meeting
- `CreateMeetingUseCase` - Создание встречи с валидацией временных слотов
- `GetUserMeetingsUseCase` - Получение встреч пользователя

#### Invitation
- `GetInvitationsUseCase` - Получение приглашений
- `RespondToInvitationUseCase` - Ответ на приглашение

#### Profile
- `GetProfileUseCase` - Получение профиля

## Data слой

### Remote API (`data/remote/api/`)

Все API интерфейсы используют Retrofit:

#### AuthApi
- `POST /api/v1/auth/register` - Регистрация
- `POST /api/v1/auth/login` - Вход
- `POST /api/v1/auth/refresh` - Обновление токенов (требует refresh токен в заголовке)
- `POST /api/v1/auth/logout` - Выход

#### ProfileApi
- `GET /api/v1/profile` - Получение профиля (требует авторизацию)
- `PUT /api/v1/profile` - Обновление профиля
- `PUT /api/v1/profile/avatar` - Обновление аватара
- `PUT /api/v1/profile/reset-password` - Сброс пароля
- `GET /api/v1/profile/public/all` - Получение всех пользователей (публичный эндпоинт)

#### MeetingApi
- `POST /api/v1/meetings` - Создание встречи
- `GET /api/v1/meetings/{meetingId}` - Получение встречи по ID
- `GET /api/v1/meetings` - Получение списка встреч (с фильтрацией)
- `PUT /api/v1/meetings/{meetingId}/cancel` - Отмена встречи
- `DELETE /api/v1/meetings/{meetingId}` - Удаление встречи
- `POST /api/v1/meetings/freeTime` - Получение свободного времени

#### InvitationApi
- `GET /api/v1/invitations` - Получение приглашений
- `GET /api/v1/invitations/{meetingId}` - Получение деталей приглашения
- `PUT /api/v1/invitations/{meetingId}/respond` - Ответ на приглашение

### DTO модели (`data/remote/dto/`)

Все DTO используют `@Serializable` для Kotlin Serialization:
- `AuthDto.kt` - модели для аутентификации
- `ProfileDto.kt` - модели для профиля
- `MeetingDto.kt` - модели для встреч
- `InvitationDto.kt` - модели для приглашений
- `PageResponse.kt` - обобщенная модель пагинации

### Mappers (`data/mapper/`)

Функции расширения для преобразования DTO → Domain:
- `AuthMapper.kt` - маппинг токенов
- `ProfileMapper.kt` - маппинг профилей
- `MeetingMapper.kt` - маппинг встреч
- `InvitationMapper.kt` - маппинг приглашений

### Local Storage (`data/local/`)

#### TokenDataStore
DataStore Preferences для хранения:
- Access токен
- Refresh токен
- Время истечения токенов
- User ID, username, email

### Interceptors (`data/remote/interceptor/`)

#### AuthInterceptor
Автоматически добавляет Bearer токен ко всем запросам:
- Access токен для защищенных эндпоинтов
- Refresh токен для `/auth/refresh`
- Пропускает токен для публичных эндпоинтов

### Repository Implementations (`data/repository/`)

Реализации интерфейсов из domain слоя:
- `AuthRepositoryImpl`
- `ProfileRepositoryImpl`
- `MeetingRepositoryImpl`
- `InvitationRepositoryImpl`

## Dependency Injection (Hilt)

### NetworkModule (`di/NetworkModule.kt`)
Предоставляет:
- `Retrofit` - настроен с Kotlin Serialization
- `OkHttpClient` - с AuthInterceptor и LoggingInterceptor
- Все API интерфейсы
- `TokenDataStore`
- `Json` конфигурация

### RepositoryModule (`di/RepositoryModule.kt`)
Связывает интерфейсы репозиториев с их реализациями

## Безопасность и авторизация

### JWT Bearer Token
- Access токен используется для всех защищенных эндпоинтов
- Refresh токен используется только для обновления токенов
- Токены хранятся в DataStore (зашифрованное хранилище)

### Автоматическое добавление токенов
`AuthInterceptor` автоматически:
1. Проверяет, нужна ли авторизация для эндпоинта
2. Получает токен из DataStore
3. Добавляет заголовок `Authorization: Bearer <token>`

### Публичные эндпоинты (без токена)
- `/api/v1/auth/login`
- `/api/v1/auth/register`
- `/api/v1/health`
- `/api/v1/profile/public/all`

## Конфигурация

### Base URL
По умолчанию: `http://localhost:8080/`
Изменить в `NetworkModule.BASE_URL`

### Timeouts
- Connect: 30 секунд
- Read: 30 секунд
- Write: 30 секунд

## Использование в ViewModel

```kotlin
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {
    
    fun login(email: String, password: String) {
        viewModelScope.launch {
            when (val result = loginUseCase(email, password)) {
                is Result.Success -> {
                    // Успешный вход
                }
                is Result.Error -> {
                    // Ошибка
                }
                is Result.Loading -> {
                    // Загрузка
                }
            }
        }
    }
}
```

## Важные особенности

### Временные слоты
- Встречи могут начинаться только ровно в начале часа (например, 9:00, 10:00)
- Валидация осуществляется в `CreateMeetingUseCase`

### Пагинация
- По умолчанию: page=0, size=20
- Используется Spring Page API (совместимо с бэкендом)

### Обработка ошибок
- Все методы репозиториев возвращают `Result<T>`
- `Result.Success` - успешный результат
- `Result.Error` - ошибка с Exception
- `Result.Loading` - состояние загрузки

### Кэширование
- `ProfileRepositoryImpl` кэширует профиль пользователя в MutableStateFlow
- Можно подписаться на изменения через `getCachedProfile()`

## Следующие шаги

Для завершения приложения необходимо:
1. Создать ViewModels для каждого экрана
2. Реализовать UI с Jetpack Compose
3. Настроить Navigation Component
4. Добавить обработку refresh токенов при 401 ошибке
5. Добавить Room Database для оффлайн режима (опционально)

