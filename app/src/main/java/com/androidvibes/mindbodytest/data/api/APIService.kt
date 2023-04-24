package com.androidvibes.mindbodytest.data.api

import com.androidvibes.mindbodytest.data.models.Country
import com.androidvibes.mindbodytest.data.models.Provinces
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/countries")
    suspend fun getCountryList(): Response<List<Country>>

    @GET("/provinces")
    suspend fun getProvincesList(@Query("country_code") code: String): Response<List<Provinces>>

}