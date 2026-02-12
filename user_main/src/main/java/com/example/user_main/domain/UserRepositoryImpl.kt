package com.example.user_main.domain


import android.util.Log
import com.example.comon.GetUserResult
import com.example.comon.User
import com.example.comon.UserMapper
import com.example.comon.UserResult
import com.example.token_storage.domain.TokenRepository
import com.example.user_main.data.UserNetworkDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userNetworkDataSource: UserNetworkDataSource,
    private val tokenRepository: TokenRepository,
    private val userMapper: UserMapper
) : UserRepository {

    private val _currentUser = MutableStateFlow<User?>(null)
    override val currentUser: StateFlow<User?> = _currentUser

    override suspend fun loadUser(): UserResult {
        val token = tokenRepository.getToken()
            ?: return UserResult.NotLoaded

        return when (val result = userNetworkDataSource.getUserByToken(token)) {
            is GetUserResult.Success -> {
                val user = userMapper.map(result.user)
                _currentUser.value = user
                UserResult.Success(user)
            }

            is GetUserResult.Error -> {
                UserResult.Error(result.message)
            }
        }
    }

    override suspend fun updateUser(user: User): UserResult {
        val token = tokenRepository.getToken()
            ?: return UserResult.Error("Пользователь не авторизован")

        return when (val result = userNetworkDataSource.updateUser(token, user)) {
            is GetUserResult.Success -> {
                val updatedUser = userMapper.map(result.user)
                _currentUser.value = updatedUser
                UserResult.Success(updatedUser)
            }

            is GetUserResult.Error -> {
                UserResult.Error(result.message)
            }
        }
    }

    override suspend fun clear() {
        _currentUser.value = null
        tokenRepository.clear()
    }
}
