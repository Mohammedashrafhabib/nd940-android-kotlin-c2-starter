package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.Constants.BASE_URL
import com.udacity.asteroidradar.PictureOfDay
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
private val  retrofit= Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()
interface NasaApiService {
    //neo/rest/v1/feed
    @GET("neo/rest/v1/feed")
     suspend fun getAsteroids(@Query("start_date") startDate: String ,@Query("end_date") endDate: String?=null,@Query("api_key") apiKey: String = API_KEY):
            ResponseBody

    @GET("planetary/apod")
    suspend fun getImage(@Query("api_key") apiKey: String = API_KEY):
            PictureOfDay

    object NasaApi {
        val retrofitService: NasaApiService by lazy {
            retrofit.create(NasaApiService::class.java)
        }
    }
}