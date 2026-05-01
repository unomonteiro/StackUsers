package io.cmcode.stackusers

import io.cmcode.stackusers.domain.model.User
import io.cmcode.stackusers.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeUserRepository : UserRepository {

    var users: List<User> = emptyList()
    var shouldFail: Boolean = false

    private val followedIds = MutableStateFlow<Set<String>>(emptySet())

    override suspend fun getTopUsers(): Result<List<User>> =
        if (shouldFail) Result.failure(RuntimeException("Network error"))
        else Result.success(users)

    override fun followedUserIds(): Flow<Set<String>> = followedIds

    override suspend fun followUser(userId: String) {
        followedIds.value = followedIds.value + userId
    }

    override suspend fun unfollowUser(userId: String) {
        followedIds.value = followedIds.value - userId
    }
}
