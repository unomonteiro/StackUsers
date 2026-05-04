package io.cmcode.stackusers

import io.cmcode.stackusers.domain.model.User
import io.cmcode.stackusers.domain.repository.UserRepository
import io.cmcode.stackusers.domain.usecase.UsersUseCase
import io.cmcode.stackusers.domain.model.UsersUiState
import io.cmcode.stackusers.ui.UsersViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.Runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UsersViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: UserRepository = mockk()

    private val sampleUsers = listOf(
        User("22656", "Jon Skeet", 1_362_987, "https://i.sstatic.net/ICsRH.jpg", "Reading, UK"),
        User("1144035", "Gordon Linoff", 701_754, "", null)
    )

    @Before
    fun setup() {
        every { repository.followedUserIds() } returns flowOf(emptySet())
        coEvery { repository.getTopUsers() } returns Result.success(sampleUsers)
    }

    @Test
    fun `uiState starts as Loading`() {
        val viewModel = UsersViewModel(UsersUseCase(repository), repository)

        assertEquals(UsersUiState.Loading, viewModel.uiState.value)
    }

    @Test
    fun `toggleFollow calls followUser when user is not followed`() = runTest {
        val viewModel = UsersViewModel(UsersUseCase(repository), repository)
        val user = sampleUsers[0].copy(isFollowed = false)
        coEvery { repository.followUser(any()) } just Runs

        viewModel.toggleFollow(user)
        advanceUntilIdle()

        coVerify { repository.followUser("22656") }
    }

    @Test
    fun `toggleFollow calls unfollowUser when user is followed`() = runTest {
        val viewModel = UsersViewModel(UsersUseCase(repository), repository)
        val user = sampleUsers[0].copy(isFollowed = true)
        coEvery { repository.unfollowUser(any()) } just Runs

        viewModel.toggleFollow(user)
        advanceUntilIdle()

        coVerify { repository.unfollowUser("22656") }
    }

}
