package com.androidvibes.mindbodytest.data.repository

import android.content.Context
import com.androidvibes.mindbodytest.R
import com.androidvibes.mindbodytest.data.NetworkResult
import com.androidvibes.mindbodytest.data.api.ApiService
import com.androidvibes.mindbodytest.data.api.BaseAPICall
import com.androidvibes.mindbodytest.data.models.Country
import com.androidvibes.mindbodytest.data.models.Provinces
import com.androidvibes.mindbodytest.utils.isInternetAvailable
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ActivityRetainedScoped
class MainRepository @Inject constructor(private val context: Context,
                                         private val apiService: ApiService,
                                         private val defaultDispatcher: CoroutineDispatcher)
    : BaseAPICall(), CommonRepository {
    override suspend fun getCountries() : NetworkResult<List<Country>> {
        return if (isInternetAvailable(context)) {
            withContext(defaultDispatcher) { safeApiCall { apiService.getCountryList() } }
        } else {
            NetworkResult.NoInternet(context.getString(R.string.internet_validation_msg))
        }
    }

    override suspend fun getProvinces(code: String) : NetworkResult<List<Provinces>> {
        return if (isInternetAvailable(context)) {
            withContext(defaultDispatcher) { safeApiCall { apiService.getProvincesList(code) } }
        } else {
            NetworkResult.NoInternet(context.getString(R.string.internet_validation_msg))
        }
    }
}