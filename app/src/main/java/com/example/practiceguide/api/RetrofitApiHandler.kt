package com.example.practiceguide.api

import com.example.practiceguide.models.Photo
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface RetrofitApiHandler {

    @GET("photos")
    fun getPhotos(): Call<List<Photo>>

    companion object {

        val API_URL = "https://jsonplaceholder.typicode.com/"

        fun create(): RetrofitApiHandler {
            val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(
                API_URL).build()
            return retrofit.create(RetrofitApiHandler::class.java)
        }
    }

}