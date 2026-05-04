package io.cmcode.stackusers.domain.usecase

import io.cmcode.stackusers.domain.repository.UserRepository
import io.cmcode.stackusers.domain.model.UsersUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UsersUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(): Flow<UsersUiState> = flow {
        emit(UsersUiState.Loading)
        repository.getTopUsers().fold(
            onSuccess = { users ->
                emitAll(
                    repository.followedUserIds().map { followedIds ->
                        UsersUiState.Success(
                            users.map { it.copy(isFollowed = it.userId in followedIds) }
                        )
                    }
                )
            },
            onFailure = { throwable ->
                emit(UsersUiState.Error(throwable.message ?: "Unknown error"))
            }
        )
    }
}
