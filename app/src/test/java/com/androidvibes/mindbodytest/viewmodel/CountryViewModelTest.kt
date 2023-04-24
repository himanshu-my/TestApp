package com.androidvibes.mindbodytest.viewmodel

import app.cash.turbine.test
import com.androidvibes.mindbodytest.data.models.ApiCallStates
import com.androidvibes.mindbodytest.repository.FakeRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CountryViewModelTest {

    // create a MainRepository mock
    private val fakeRepository = FakeRepository()

    // create a ViewModel instance
    private lateinit var viewModel: CountryViewModel

    @ExperimentalCoroutinesApi
    private val testDispatcher = StandardTestDispatcher()

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        // initialize the ViewModel with the mock MainRepository
        viewModel = CountryViewModel(fakeRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test fetchCountries when internet is available`(): Unit = runTest {

        viewModel.fetchCountries()

        viewModel.uiState.test {
            val item1 = awaitItem()
            println(item1)

            Assert.assertEquals(
                ApiCallStates.InProgress,
                item1
            )

            val item3 = awaitItem()
            println(item3)

            if (item3 is ApiCallStates.CountryListSuccess) {
                Assert.assertEquals(
                    ApiCallStates.CountryListSuccess(countryList = item3.countryList)::class,
                    item3::class
                )
                assertThat(item3.countryList.isNotEmpty()).isTrue()

            } else if (item3 is ApiCallStates.ApiError) {
                Assert.assertEquals(
                    ApiCallStates.ApiError(item3.message)::class,
                    item3::class
                )
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test fetchProvinces when internet is available`(): Unit = runTest {

        viewModel.fetchProvinces("IN")

        viewModel.uiState.test {
            val item1 = awaitItem()
            println(item1)

            Assert.assertEquals(
                ApiCallStates.InProgress,
                item1
            )

            val item3 = awaitItem()
            println(item3)

            if (item3 is ApiCallStates.ProvincesListSuccess) {
                Assert.assertEquals(
                    ApiCallStates.ProvincesListSuccess(provincesList = item3.provincesList)::class,
                    item3::class
                )
                assertThat(item3.provincesList.isNotEmpty()).isTrue()

            } else if (item3 is ApiCallStates.ApiError) {
                Assert.assertEquals(
                    ApiCallStates.ApiError(item3.message)::class,
                    item3::class
                )
            }
        }
    }
}