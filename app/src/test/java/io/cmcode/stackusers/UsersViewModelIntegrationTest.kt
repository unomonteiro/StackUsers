package io.cmcode.stackusers

import app.cash.turbine.test
import io.cmcode.stackusers.domain.model.User
import io.cmcode.stackusers.domain.usecase.UsersUseCase
import io.cmcode.stackusers.domain.model.UsersUiState
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UsersViewModelIntegrationTest {

    private val fakeRepository = FakeUserRepository()
    private val useCase = UsersUseCase(fakeRepository)

    private val sampleUsers = listOf(
        User("22656", "Jon Skeet", 1_362_987, "https://i.sstatic.net/ICsRH.jpg", "Reading, UK"),
        User("1144035", "Gordon Linoff", 701_754, "", null)
    )

    @Before
    fun setup() {
        fakeRepository.users = sampleUsers
    }

    @Test
    fun `emits Loading then Success with correct users`() = runTest {
        useCase().test {
            assertEquals(UsersUiState.Loading, awaitItem())
            val success = awaitItem() as UsersUiState.Success
            assertEquals(2, success.users.size)
            cancel()
        }
    }

    @Test
    fun `emits Loading then Error when repository fails`() = runTest {
        fakeRepository.shouldFail = true

        useCase().test {
            assertEquals(UsersUiState.Loading, awaitItem())
            val error = awaitItem() as UsersUiState.Error
            assertEquals("Network error", error.message)
            awaitComplete()
        }
    }

    @Test
    fun `re-emits Success reactively when follow state changes`() = runTest {
        useCase().test {
            awaitItem() // Loading
            val initial = awaitItem() as UsersUiState.Success
            assertFalse(initial.users[0].isFollowed)

            fakeRepository.followUser("22656")

            val updated = awaitItem() as UsersUiState.Success
            assertTrue(updated.users[0].isFollowed)
            cancel()
        }
    }
}
