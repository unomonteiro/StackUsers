package io.cmcode.stackusers

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.cmcode.stackusers.domain.model.User
import io.cmcode.stackusers.domain.model.UsersUiState
import io.cmcode.stackusers.ui.UsersContent
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UsersScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val sampleUsers = listOf(
        User("22656", "Jon Skeet", 1_362_987, "https://i.sstatic.net/ICsRH.jpg", "Reading, UK"),
        User("1144035", "Gordon Linoff", 701_754, "", null)
    )

    @Test
    fun loading_showsProgressIndicator() {
        composeTestRule.setContent {
            UsersContent(uiState = UsersUiState.Loading, onToggleFollow = {}, onRetry = {})
        }

        composeTestRule.onNodeWithTag("loading_indicator").assertIsDisplayed()
    }

    @Test
    fun success_showsUserNames() {
        composeTestRule.setContent {
            UsersContent(uiState = UsersUiState.Success(sampleUsers), onToggleFollow = {}, onRetry = {})
        }

        composeTestRule.onNodeWithText("Jon Skeet").assertIsDisplayed()
        composeTestRule.onNodeWithText("Gordon Linoff").assertIsDisplayed()
    }

    @Test
    fun error_showsErrorMessageAndRetryButton() {
        composeTestRule.setContent {
            UsersContent(uiState = UsersUiState.Error("Network error"), onToggleFollow = {}, onRetry = {})
        }

        composeTestRule.onNodeWithText("Network error").assertIsDisplayed()
        composeTestRule.onNodeWithText("Retry").assertIsDisplayed()
    }

    @Test
    fun error_retryButton_triggersCallback() {
        var retried = false

        composeTestRule.setContent {
            UsersContent(
                uiState = UsersUiState.Error("Network error"),
                onToggleFollow = {},
                onRetry = { retried = true }
            )
        }

        composeTestRule.onNodeWithText("Retry").performClick()

        assertEquals(true, retried)
    }

    @Test
    fun followButton_triggersCallbackWithCorrectUser() {
        var toggledUser: User? = null

        composeTestRule.setContent {
            UsersContent(
                uiState = UsersUiState.Success(sampleUsers),
                onToggleFollow = { toggledUser = it },
                onRetry = {}
            )
        }

        composeTestRule.onAllNodesWithText("Follow").onFirst().performClick()

        assertEquals(sampleUsers[0], toggledUser)
    }
}
