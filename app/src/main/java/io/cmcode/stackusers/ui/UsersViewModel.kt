package io.cmcode.stackusers.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.cmcode.stackusers.domain.model.User
import io.cmcode.stackusers.domain.repository.UserRepository
import io.cmcode.stackusers.domain.usecase.UsersUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface UsersUiState {
    data object Loading : UsersUiState
    data class Success(val users: List<User>) : UsersUiState
    data class Error(val message: String) : UsersUiState
}

@HiltViewModel
class UsersViewModel @Inject constructor(
    usersUseCase: UsersUseCase,
    private val repository: UserRepository
) : ViewModel() {

    val uiState: StateFlow<UsersUiState> = usersUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UsersUiState.Loading
        )

    fun toggleFollow(user: User) {
        viewModelScope.launch {
            Log.d("StackUsers", "toggleFollow: ${user.userId} followed=${user.isFollowed}")
            if (user.isFollowed) {
                repository.unfollowUser(user.userId)
            } else {
                repository.followUser(user.userId)
            }
        }
    }
}
