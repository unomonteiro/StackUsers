package io.cmcode.stackusers.data.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FollowStorage @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val followedKey = stringSetPreferencesKey("followed_user_ids")

    val followedUserIds: Flow<Set<String>> = dataStore.data.map { prefs ->
        prefs[followedKey] ?: emptySet()
    }

    suspend fun follow(userId: String) {
        dataStore.edit { prefs ->
            val current = prefs[followedKey] ?: emptySet()
            prefs[followedKey] = current + userId
        }
    }

    suspend fun unfollow(userId: String) {
        dataStore.edit { prefs ->
            val current = prefs[followedKey] ?: emptySet()
            prefs[followedKey] = current - userId
        }
    }
}
