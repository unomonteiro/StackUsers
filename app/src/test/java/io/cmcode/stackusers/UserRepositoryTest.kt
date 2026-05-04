package io.cmcode.stackusers

import io.cmcode.stackusers.data.api.StackOverflowApi
import io.cmcode.stackusers.data.model.UserDto
import io.cmcode.stackusers.data.model.UsersResponse
import io.cmcode.stackusers.data.repository.UserRepositoryImpl
import io.cmcode.stackusers.data.storage.FollowStorage
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.Runs
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UserRepositoryTest {

    private val api: StackOverflowApi = mockk()
    private val followStorage: FollowStorage = mockk()
    private lateinit var repository: UserRepositoryImpl

    private val sampleDtos = listOf(
        UserDto(22656, "Jon Skeet", 1_362_987, "https://i.sstatic.net/ICsRH.jpg", "Reading, UK"),
        UserDto(1144035, "Gordon Linoff", 701_754, null, null)
    )

    @Before
    fun setup() {
        repository = UserRepositoryImpl(api, followStorage)
    }

    @Test
    fun `getTopUsers returns mapped users from api`() = runTest {
        coEvery { api.getTopUsers() } returns UsersResponse(sampleDtos)

        val users = repository.getTopUsers().getOrThrow()

        assertEquals(2, users.size)
        assertEquals("Jon Skeet", users[0].displayName)
    }

    @Test
    fun `getTopUsers returns failure when api throws`() = runTest {
        coEvery { api.getTopUsers() } throws RuntimeException("Network timeout")

        val result = repository.getTopUsers()

        assertTrue(result.isFailure)
        assertEquals("Network timeout", result.exceptionOrNull()?.message)
    }

    @Test
    fun `followedUserIds delegates to FollowStorage`() = runTest {
        val expected = setOf("1", "2")
        every { followStorage.followedUserIds } returns flowOf(expected)

        val result = repository.followedUserIds().first()

        assertEquals(expected, result)
    }

    @Test
    fun `followUser delegates to FollowStorage`() = runTest {
        coEvery { followStorage.follow(any()) } just Runs

        repository.followUser("42")

        coVerify { followStorage.follow("42") }
    }

    @Test
    fun `unfollowUser delegates to FollowStorage`() = runTest {
        coEvery { followStorage.unfollow(any()) } just Runs

        repository.unfollowUser("42")

        coVerify { followStorage.unfollow("42") }
    }
}

