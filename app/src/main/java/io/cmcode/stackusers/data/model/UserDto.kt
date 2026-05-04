package io.cmcode.stackusers.data.model

import com.google.gson.annotations.SerializedName
import io.cmcode.stackusers.domain.model.User

data class UserDto(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("display_name") val displayName: String,
    @SerializedName("reputation") val reputation: Int,
    @SerializedName("profile_image") val profileImage: String?,
    @SerializedName("location") val location: String?
) {
    fun toDomainModel(): User = User(
        userId = userId.toString(),
        displayName = displayName,
        reputation = reputation,
        profileImage = profileImage.orEmpty(),
        location = location
    )
}

data class UsersResponse(
    @SerializedName("items") val items: List<UserDto>
)
