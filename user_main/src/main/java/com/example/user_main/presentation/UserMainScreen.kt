package com.example.user_main.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.comon.ErrorState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: UserMainScreenViewModel,
    onLogoutClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val isDialogVisible by viewModel.isEditDialogVisible.collectAsState()

    val editFullName by viewModel.editFullName.collectAsState()
    val editDepartment by viewModel.editDepartment.collectAsState()

    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val pullState = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = viewModel::refresh,
        state = pullState,
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Spacer(modifier = Modifier.height(150.dp))
                Box(modifier = Modifier.fillMaxSize()) {

                    when (uiState) {
                        is UserUiState.Loading -> {
                            ProfileContentShimmer()
                        }

                        is UserUiState.Error -> {
                            ErrorState(
                                onRefresh = viewModel::refresh,
                                message = (uiState as UserUiState.Error).message
                            )
                        }

                        is UserUiState.Success -> {
                            val user = (uiState as UserUiState.Success).user
                            ProfileContent(
                                user = user,
                                onEditClick = viewModel::openEditDialog,
                                onLogoutClick = {
                                    onLogoutClick()
                                    viewModel.logout()
                                }
                            )
                        }

                        is UserUiState.NotLoaded -> {
                            LaunchedEffect(Unit) {
                                viewModel.loadUser()
                            }
                        }
                    }

                    if (isDialogVisible) {
                        EditUserDialog(
                            fullName = editFullName,
                            department = editDepartment,
                            onFullNameChange = viewModel::onFullNameChange,
                            onDepartmentChange = viewModel::onDepartmentChange,
                            onDismiss = viewModel::closeEditDialog,
                            onSave = {
                                viewModel.saveChanges()
                                viewModel.closeEditDialog()
                            }
                        )
                    }
                }
            }
        }

    }
}
