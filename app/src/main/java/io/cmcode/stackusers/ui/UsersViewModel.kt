package io.cmcode.stackusers.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.cmcode.stackusers.domain.model.User
import io.cmcode.stackusers.domain.model.UsersUiState
import io.cmcode.stackusers.domain.repository.UserRepository
import io.cmcode.stackusers.domain.usecase.UsersUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class UsersViewModel @Inject constructor(
    private val usersUseCase: UsersUseCase,
    private val repository: UserRepository
) : ViewModel() {

    private val retryTrigger = MutableStateFlow(0)

    val uiState: StateFlow<UsersUiState> = retryTrigger
        .flatMapLatest { usersUseCase() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UsersUiState.Loading
        )

    fun toggleFollow(user: User) {
        viewModelScope.launch {
            if (user.isFollowed) {
                repository.unfollowUser(user.userId)
            } else {
                repository.followUser(user.userId)
            }
        }
    }

    fun retry() {
        retryTrigger.value++
    }
}
