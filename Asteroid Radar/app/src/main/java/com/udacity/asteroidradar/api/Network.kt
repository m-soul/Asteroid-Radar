package com.udacity.asteroidradar.api


import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.Constants
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
private val retrofit = Retrofit.Builder().addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(Constants.BASE_URL).build()

interface NetworkInterface {
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(@Query("api_key") apikey : String): String
    @GET("planetary/apod")
    suspend fun getImageOfTheDay(@Query("api_key") apikey : String) : String
}

object Network {
    val api: NetworkInterface by lazy { retrofit.create(NetworkInterface::class.java) }
}
