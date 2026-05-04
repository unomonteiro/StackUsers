package io.cmcode.stackusers.domain.model

data class User(
    val userId: String,
    val displayName: String,
    val reputation: Int,
    val profileImage: String,
    val location: String?,
    val isFollowed: Boolean = false
)
