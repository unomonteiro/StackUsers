package io.cmcode.stackusers

import io.cmcode.stackusers.data.model.UserDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Test

class UserDtoTest {

    @Test
    fun `toDomainModel converts userId Int to String`() {
        val dto = UserDto(22656, "Jon Skeet", 1_362_987, null, null)

        assertEquals("22656", dto.toDomainModel().userId)
    }

    @Test
    fun `toDomainModel maps null profileImage to empty string`() {
        val dto = UserDto(1144035, "Gordon Linoff", 701_754, null, null)

        assertEquals("", dto.toDomainModel().profileImage)
    }

    @Test
    fun `toDomainModel preserves profileImage url`() {
        val url = "https://i.sstatic.net/ICsRH.jpg"
        val dto = UserDto(22656, "Jon Skeet", 1_362_987, url, null)

        assertEquals(url, dto.toDomainModel().profileImage)
    }

    @Test
    fun `toDomainModel preserves nullable location`() {
        val dto = UserDto(1144035, "Gordon Linoff", 701_754, null, null)

        assertNull(dto.toDomainModel().location)
    }

    @Test
    fun `toDomainModel maps all fields correctly`() {
        val dto = UserDto(22656, "Jon Skeet", 1_362_987, "https://i.sstatic.net/ICsRH.jpg", "Reading, UK")

        val user = dto.toDomainModel()

        assertEquals("22656", user.userId)
        assertEquals("Jon Skeet", user.displayName)
        assertEquals(1_362_987, user.reputation)
        assertEquals("https://i.sstatic.net/ICsRH.jpg", user.profileImage)
        assertEquals("Reading, UK", user.location)
        assertFalse(user.isFollowed)
    }
}
