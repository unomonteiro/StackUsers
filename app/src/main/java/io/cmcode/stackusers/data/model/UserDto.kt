package io.cmcode.stackusers.data.model

import com.google.gson.annotations.SerializedName
import io.cmcode.stackusers.domain.model.User

data class UserDto(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("display_name") val displeyName: String,
    @SerializedName("reputation") val reputation: Int,
    @SerializedName("profile_image") val profileImage: String?,
    @SerializedName("location") val location: String?,
    @SerializedName("answer_count") val answerCount: Int = 0,
    @SerializedName("question_count") val questionCount: Int = 0
) {
    fun toDomainModel(): User = User(
        userId = userId.toString(),
        displayName = displeyName,
        reputation = reputation,
        profileImage = profileImage.orEmpty(),
        location = location,
        answerCount = answerCount,
        questionCount = questionCount
    )
}

data class UsersResponse(
    @SerializedName("items") val items: List<UserDto>
)
