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
import io.cmcode.stackusers.ui.UsersContent
import io.cmcode.stackusers.ui.UsersUiState
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UsersScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val sampleUsers = listOf(
        User("1", "Jon Skeet", 1_400_000, "", "Reading, UK", 35000, 58),
        User("2", "Gordon Linoff", 700_000, "", null, 30000, 12)
    )

    @Test
    fun loading_showsProgressIndicator() {
        composeTestRule.setContent {
            UsersContent(uiState = UsersUiState.Loading, onToggleFollow = {})
        }

        composeTestRule.onNodeWithTag("loading_indicator").assertIsDisplayed()
    }

    @Test
    fun success_showsUserNames() {
        composeTestRule.setContent {
            UsersContent(uiState = UsersUiState.Success(sampleUsers), onToggleFollow = {})
        }

        composeTestRule.onNodeWithText("Jon Skeet").assertIsDisplayed()
        composeTestRule.onNodeWithText("Gordon Linoff").assertIsDisplayed()
    }

    @Test
    fun error_showsErrorMessage() {
        composeTestRule.setContent {
            UsersContent(uiState = UsersUiState.Error("Network error"), onToggleFollow = {})
        }

        composeTestRule.onNodeWithText("Network error").assertIsDisplayed()
    }

    @Test
    fun followButton_triggersCallbackWithCorrectUser() {
        var toggledUser: User? = null

        composeTestRule.setContent {
            UsersContent(
                uiState = UsersUiState.Success(sampleUsers),
                onToggleFollow = { toggledUser = it }
            )
        }

        composeTestRule.onAllNodesWithText("Follow").onFirst().performClick()

        assertEquals(sampleUsers[0], toggledUser)
    }
}
