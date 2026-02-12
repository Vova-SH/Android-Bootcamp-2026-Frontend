package ru.sicampus.bootcamp2026.screens.Profile

//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import ru.sicampus.bootcamp2026.data.UserRepository
//import ru.sicampus.bootcamp2026.data.source.UserInfoDataSource
//
//class ProfileViewModelFactory : ViewModelProvider.Factory {
//
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
//            val userRepository = UserRepository(
//                userInfoDataSource = UserInfoDataSource()
//            )
//            return ProfileViewModel(userRepository) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}