package com.example.planify.main.navigation.screens.main_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planify.main.features.actions.domain.entities.Action
import com.example.planify.main.features.actions.domain.services.ActionsService
import com.example.planify.main.features.actions.domain.utils.ActionDataParser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val actionsService: ActionsService
) : ViewModel() {
//    init {
//        viewModelScope.launch {
//            actionsService.actionsFlow.collect(::onAction)
//        }
//    }
//
//    @Suppress("UNCHECKED_CAST")
//    fun onAction(action: Action<*>) {
//        Log.i("Actions", "${action.type}: ${action.id}")
//    }
}
