package com.example.firstapp.data.repository

import com.example.firstapp.data.api.ApiHelper

class MainRepository(private val apiHelper: ApiHelper) {
    suspend fun getGifs(apiKey: String, query: String, offset: Int) =
        apiHelper.getGifs(apiKey, query, offset)
}

