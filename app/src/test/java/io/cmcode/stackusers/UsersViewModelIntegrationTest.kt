package io.cmcode.stackusers

import app.cash.turbine.test
import io.cmcode.stackusers.domain.model.User
import io.cmcode.stackusers.domain.usecase.UsersUseCase
import io.cmcode.stackusers.ui.UsersUiState
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
        User("1", "Jon Skeet", 1_400_000, "", "Reading, UK", 35000, 58),
        User("2", "Gordon Linoff", 700_000, "", null, 30000, 12)
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

            fakeRepository.followUser("1")

            val updated = awaitItem() as UsersUiState.Success
            assertTrue(updated.users[0].isFollowed)
            cancel()
        }
    }
}
