package com.example.firstapp.data.api

import com.example.firstapp.data.model.GifResponse

class ApiHelper(private val apiService: ApiService) {
    suspend fun getGifs(apiKey: String, query: String, offset: Int): GifResponse {


        return apiService.getGifs(apiKey, query, offset)
    }
}
