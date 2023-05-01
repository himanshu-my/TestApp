package com.androidvibes.mindbodytest.viewmodel

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidvibes.mindbodytest.data.NetworkResult
import com.androidvibes.mindbodytest.data.models.ApiCallStates
import com.androidvibes.mindbodytest.data.models.Country
import com.androidvibes.mindbodytest.data.models.Provinces
import com.androidvibes.mindbodytest.data.repository.CommonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountryViewModel @Inject constructor(
    private val mainRepository: CommonRepository
) : ViewModel(), LifecycleObserver {


    private val _uiState = MutableStateFlow<ApiCallStates>(ApiCallStates.InProgress)
    val uiState = _uiState.asStateFlow()

    private val _countryCode = MutableStateFlow("")
    val countryCode = _countryCode.asStateFlow()

    var countryScreenList: List<Country>? = null
    var provinceScreenList: List<Provinces>? = null

    fun setCountryCode(code: String) {
        viewModelScope.launch {
            _countryCode.value = code
        }
    }

    fun setCountryList(list: List<Country>) {
        viewModelScope.launch {
            _uiState.value = ApiCallStates.CountryListSuccess(list)
        }
    }

    fun setProvincesList(list: List<Provinces>) {
        viewModelScope.launch {
            _uiState.value = ApiCallStates.ProvincesListSuccess(list)
        }
    }

    fun fetchCountries() = viewModelScope.launch(Dispatchers.IO) {
        when (val result = mainRepository.getCountries()) {
            is NetworkResult.Success -> {
                result.data?.let {
                    _uiState.value = ApiCallStates.CountryListSuccess(it)
                    countryScreenList = it
                }
            }
            is NetworkResult.Error -> {
                result.message?.let {
                    _uiState.value = ApiCallStates.ApiError(it)
                }
            }
            is NetworkResult.NoInternet -> {
                _uiState.value = ApiCallStates.NetworkError(
                    result.message
                )
            }
        }

    }

    fun fetchProvinces(countryCode: String) = viewModelScope.launch(Dispatchers.IO) {
        _uiState.value = ApiCallStates.InProgress
        when (val result = mainRepository.getProvinces(countryCode)) {
            is NetworkResult.Success -> {
                result.data?.let {
                    _uiState.value = ApiCallStates.ProvincesListSuccess(it)
                    provinceScreenList = it
                }
            }
            is NetworkResult.Error -> {
                result.message?.let {
                    _uiState.value = ApiCallStates.ApiError(it)
                }
            }
            is NetworkResult.NoInternet -> {
                _uiState.value = ApiCallStates.NetworkError(
                    result.message
                )
            }
        }
    }

    // Save the state of country and province list in a Bundle

    fun saveCountryListState(): Bundle {
        val bundle = Bundle()
        bundle.putParcelableArrayList(
            "provinceScreenList",
            provinceScreenList as? ArrayList<out Parcelable>
        )
        return bundle
    }

    fun saveProvincesListState(): Bundle {
        val bundle = Bundle()
        bundle.putParcelableArrayList(
            "provinceScreenList",
            provinceScreenList as? ArrayList<out Parcelable>
        )
        return bundle
    }

    // Restore the state of country and province list from a Bundle

    fun restoreCountryListState(bundle: Bundle?) {
        if (bundle != null) {
            val countryScreenList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelableArrayList("countryScreenList", Country::class.java)
            } else {
                bundle.getParcelableArrayList("countryScreenList")
            }
            if (countryScreenList != null) {
                setCountryList(countryScreenList)
            }
        }
    }

    fun restoreProvincesListState(bundle: Bundle?) {
        if (bundle != null) {
            val provinceScreenList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelableArrayList("provinceScreenList", Provinces::class.java)
            } else {
                bundle.getParcelableArrayList("provinceScreenList")
            }
            if (provinceScreenList != null) {
                setProvincesList(provinceScreenList)
            }
        }
    }
}