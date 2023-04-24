package com.androidvibes.mindbodytest.repository

import com.androidvibes.mindbodytest.data.NetworkResult
import com.androidvibes.mindbodytest.data.models.Country
import com.androidvibes.mindbodytest.data.models.Provinces
import com.androidvibes.mindbodytest.data.repository.CommonRepository

class FakeRepository : CommonRepository {

    override suspend fun getCountries():
            NetworkResult<List<Country>> =
        NetworkResult.Success(
            listOf(
                Country("CA", 1, "Canada", 1),
                Country("US", 2, "United States", 2)
            )
        )

    override suspend fun getProvinces(code: String):
            NetworkResult<List<Provinces>> =
        NetworkResult.Success(
            listOf(
                Provinces("MP", 1, "Madhya Pradesh"),
                Provinces("UP", 2, "Uttar Pradesh")
            )
        )
}