package io.cmcode.stackusers

import io.cmcode.stackusers.data.model.UserDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Test

class UserDtoTest {

    @Test
    fun `toDomainModel converts userId Int to String`() {
        val dto = UserDto(42, "Jon Skeet", 1_400_000, null, null, 0, 0)

        assertEquals("42", dto.toDomainModel().userId)
    }

    @Test
    fun `toDomainModel maps null profileImage to empty string`() {
        val dto = UserDto(1, "Jon Skeet", 0, null, null, 0, 0)

        assertEquals("", dto.toDomainModel().profileImage)
    }

    @Test
    fun `toDomainModel preserves profileImage url`() {
        val url = "https://example.com/avatar.jpg"
        val dto = UserDto(1, "Jon Skeet", 0, url, null, 0, 0)

        assertEquals(url, dto.toDomainModel().profileImage)
    }

    @Test
    fun `toDomainModel preserves nullable location`() {
        val dto = UserDto(1, "Jon Skeet", 0, null, null, 0, 0)

        assertNull(dto.toDomainModel().location)
    }

    @Test
    fun `toDomainModel maps all fields correctly`() {
        val dto = UserDto(7, "Jon Skeet", 1_400_000, "https://img.com/pic.png", "Reading, UK", 35000, 58)

        val user = dto.toDomainModel()

        assertEquals("7", user.userId)
        assertEquals("Jon Skeet", user.displayName)
        assertEquals(1_400_000, user.reputation)
        assertEquals("https://img.com/pic.png", user.profileImage)
        assertEquals("Reading, UK", user.location)
        assertEquals(35000, user.answerCount)
        assertEquals(58, user.questionCount)
        assertFalse(user.isFollowed)
    }
}
