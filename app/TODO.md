Экран Notification

## Авторизация
EncryptedSharedPreferences для refresh token

## Data Layer
Определение API: Создать интерфейсы Retrofit в пакете data/api (например, AuthApi, UserApi), используя готовые DTO из data/dto.
Сетевой клиент: Реализовать Singleton или модуль (DI) для создания экземпляра Retrofit с базовым URL и OkHttp клиентом.

## Repository
Проектирование интерфейсов репозиториев: Наполнить пустые интерфейсы в data/repository методами (например, login, getProfile), возвращающими Result<DomainModel>.
Реализация репозиториев: Создать классы реализации (например, AuthRepositoryImpl) в data/repository, которые вызывают API и преобразуют DTO в Domain модели через AuthMapper.

## Domain Layer
Перенос интерфейсов (Refactoring): Переместить интерфейсы репозиториев из data в domain/repository для соблюдения Clean Architecture.