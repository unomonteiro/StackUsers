package io.cmcode.stackusers

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import io.cmcode.stackusers.data.storage.FollowStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FollowStorageTest {

    private val prefsFlow = MutableStateFlow(emptyPreferences())

    private val fakeDataStore = object : DataStore<Preferences> {
        override val data: Flow<Preferences> = prefsFlow
        override suspend fun updateData(transform: suspend (t: Preferences) -> Preferences): Preferences {
            val updated = transform(prefsFlow.value)
            prefsFlow.value = updated
            return updated
        }
    }

    private lateinit var storage: FollowStorage

    @Before
    fun setup() {
        storage = FollowStorage(fakeDataStore)
    }

    @Test
    fun `followedUserIds emits empty set when nothing is stored`() = runTest {
        val result = storage.followedUserIds.first()

        assertEquals(emptySet<String>(), result)
    }

    @Test
    fun `follow adds userId to the stored set`() = runTest {
        storage.follow("1")

        assertTrue(storage.followedUserIds.first().contains("1"))
    }

    @Test
    fun `following multiple users accumulates all ids`() = runTest {
        storage.follow("1")
        storage.follow("2")

        assertEquals(setOf("1", "2"), storage.followedUserIds.first())
    }

    @Test
    fun `unfollow removes userId from the stored set`() = runTest {
        storage.follow("1")
        storage.follow("2")
        storage.unfollow("1")

        assertEquals(setOf("2"), storage.followedUserIds.first())
    }

    @Test
    fun `unfollow a non-existent id is a no-op`() = runTest {
        storage.follow("1")
        storage.unfollow("99")

        assertEquals(setOf("1"), storage.followedUserIds.first())
    }
}
