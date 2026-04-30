package io.cmcode.stackusers.data.api

import io.cmcode.stackusers.data.model.UsersResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface StackOverflowApi {
    @GET("users")
    suspend fun getTopUsers(
        @Query("order") order: String = "desc",
        @Query("sort") sort: String = "reputation",
        @Query("site") site: String = "stackoverflow",
        @Query("pagesize") pageSize: Int = 30
    ): UsersResponse
}
