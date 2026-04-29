package io.cmcode.stackusers.data.repository

import io.cmcode.stackusers.data.api.StackOverflowApi
import io.cmcode.stackusers.data.storage.FollowStorage
import io.cmcode.stackusers.domain.model.User
import io.cmcode.stackusers.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: StackOverflowApi,
    private val followStorage: FollowStorage
) : UserRepository {

    override suspend fun getTopUsers(): Result<List<User>> = runCatching {
        // TODO: add caching
        api.getTopUsers().items.map { it.toDomainModel() }
    }

    override fun followedUserIds(): Flow<Set<String>> = followStorage.followedUserIds

    override suspend fun followUser(userId: String) = followStorage.follow(userId)

    override suspend fun unfollowUser(userId: String) = followStorage.unfollow(userId)
}
