package io.cmcode.stackusers.domain.repository

import io.cmcode.stackusers.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getTopUsers(): Result<List<User>>
    fun followedUserIds(): Flow<Set<String>>
    suspend fun followUser(userId: String)
    suspend fun unfollowUser(userId: String)
}
