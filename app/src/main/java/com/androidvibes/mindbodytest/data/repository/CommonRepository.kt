package com.androidvibes.mindbodytest.data.repository

import com.androidvibes.mindbodytest.data.NetworkResult
import com.androidvibes.mindbodytest.data.models.Country
import com.androidvibes.mindbodytest.data.models.Provinces

interface CommonRepository {

    suspend fun getCountries() : NetworkResult<List<Country>>

    suspend fun getProvinces(code: String) : NetworkResult<List<Provinces>>
}