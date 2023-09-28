package com.example.servicetutorial

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface DemoApi {
    @GET("posts/1")
    suspend fun getPost(): Response<Post>
}