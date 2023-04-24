package com.androidvibes.mindbodytest.data.models

sealed class ApiCallStates {

    data class CountryListSuccess(
        val countryList: List<Country>)
        : ApiCallStates()

    data class ProvincesListSuccess(
        val provincesList: List<Provinces>)
        : ApiCallStates()

    object InProgress: ApiCallStates()

    data class ApiError(val message: String?)
        : ApiCallStates()

    data class NetworkError(val message: String?)
        : ApiCallStates()
}
