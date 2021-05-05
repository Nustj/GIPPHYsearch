package com.example.firstapp.data.api

import com.example.firstapp.data.model.GifResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("gifs/search")
    suspend fun getGifs(
        @Query("api_key") apiKey: String,
        @Query("q") query: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int = 25
    ): GifResponse

}
