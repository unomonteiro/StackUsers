package io.cmcode.stackusers

import io.cmcode.stackusers.domain.model.User
import io.cmcode.stackusers.domain.repository.UserRepository
import io.cmcode.stackusers.domain.usecase.UsersUseCase
import io.cmcode.stackusers.domain.model.UsersUiState
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UsersUseCaseTest {

    private lateinit var repository: UserRepository
    private lateinit var useCase: UsersUseCase

    private val sampleUsers = listOf(
        User("22656", "Jon Skeet", 1_362_987, "https://i.sstatic.net/ICsRH.jpg", "Reading, UK"),
        User("1144035", "Gordon Linoff", 701_754, "", null)
    )

    @Before
    fun setup() {
        repository = mockk()
        useCase = UsersUseCase(repository)
    }

    @Test
    fun `first emission is Loading`() = runTest {
        every { repository.followedUserIds() } returns flowOf(emptySet())
        coEvery { repository.getTopUsers() } returns Result.success(sampleUsers)

        val first = useCase().first()

        assertEquals(UsersUiState.Loading, first)
    }

    @Test
    fun `emits Success when repo returns users`() = runTest {
        every { repository.followedUserIds() } returns flowOf(emptySet())
        coEvery { repository.getTopUsers() } returns Result.success(sampleUsers)

        val emissions = useCase().toList()

        assertTrue(emissions[0] is UsersUiState.Loading)
        val success = emissions[1] as UsersUiState.Success
        assertEquals(2, success.users.size)
        assertEquals("Jon Skeet", success.users[0].displayName)
    }

    @Test
    fun `emits Error when repository fails`() = runTest {
        every { repository.followedUserIds() } returns flowOf(emptySet())
        coEvery { repository.getTopUsers() } returns Result.failure(Exception("Network error"))

        val emissions = useCase().toList()

        assertTrue(emissions[0] is UsersUiState.Loading)
        val error = emissions[1] as UsersUiState.Error
        assertEquals("Network error", error.message)
    }

    @Test
    fun `marks followed users correctly`() = runTest {
        every { repository.followedUserIds() } returns flowOf(setOf("22656"))
        coEvery { repository.getTopUsers() } returns Result.success(sampleUsers)

        val emissions = useCase().toList()

        val success = emissions[1] as UsersUiState.Success
        assertTrue(success.users[0].isFollowed)
        assertFalse(success.users[1].isFollowed)
    }

    @Test
    fun `re-emits Success when follow state changes`() = runTest {
        every { repository.followedUserIds() } returns flowOf(emptySet(), setOf("1144035"))
        coEvery { repository.getTopUsers() } returns Result.success(sampleUsers)

        val emissions = useCase().toList()

        assertEquals(3, emissions.size)
        val first = emissions[1] as UsersUiState.Success
        val second = emissions[2] as UsersUiState.Success
        assertFalse(first.users[1].isFollowed)
        assertTrue(second.users[1].isFollowed)
    }
}
