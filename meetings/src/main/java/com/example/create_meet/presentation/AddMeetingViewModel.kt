package com.example.create_meet.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.comon.UserDto
import com.example.create_meet.data.dto.CreateMeetingDto
import com.example.create_meet.domain.use_cases.CreateMeetingUseCase
import com.example.create_meet.domain.use_cases.GetUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@HiltViewModel
class AddMeetingViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val createMeetingUseCase: CreateMeetingUseCase
) : ViewModel() {

    var title by mutableStateOf("")
    var description by mutableStateOf("")
    var location by mutableStateOf("")
    var durationHours by mutableIntStateOf(1)
    var selectedUsers by mutableStateOf<List<String>>(emptyList())

    var users by mutableStateOf<List<UserDto>>(emptyList())
    var usersLoading by mutableStateOf(false)
    var usersError by mutableStateOf<String?>(null)

    var isSubmitting by mutableStateOf(false)
    var submitError by mutableStateOf<String?>(null)
    var submitSuccess by mutableStateOf(false)

    init {
        loadUsers()
    }
    var dateInput by mutableStateOf("")
    var timeInput by mutableStateOf("")

    var dateError by mutableStateOf<String?>(null)
    var timeError by mutableStateOf<String?>(null)

    fun validateAndGetIso(): String? {
        dateError = null
        timeError = null

        val date = try {
            LocalDate.parse(dateInput, DateTimeFormatter.ISO_LOCAL_DATE)
        } catch (e: Exception) {
            dateError = "Неверная дата"
            return null
        }

        val time = try {
            LocalTime.parse(timeInput, DateTimeFormatter.ofPattern("HH:mm"))
        } catch (e: Exception) {
            timeError = "Неверное время"
            return null
        }

        return date.atTime(time)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toString()
    }
    private fun loadUsers() {
        viewModelScope.launch {
            usersLoading = true
            usersError = null
            getUsersUseCase().onSuccess {
                users = it
            }.onFailure {
                usersError = it.message
            }
            usersLoading = false
        }
    }

    fun onSubmit() {
        val isoString = validateAndGetIso() ?: return

        viewModelScope.launch {
            isSubmitting = true
            submitError = null

            val dto = CreateMeetingDto(
                title = title.trim(),
                description = description.trim(),
                location = location.trim(),
                startTime = isoString,
                durationHours = durationHours,
                invitees = selectedUsers
            )
            Log.d("DTO","$dto")

            createMeetingUseCase(dto)
                .onSuccess { submitSuccess = true }
                .onFailure { submitError = it.message }

            isSubmitting = false
        }
    }

}
