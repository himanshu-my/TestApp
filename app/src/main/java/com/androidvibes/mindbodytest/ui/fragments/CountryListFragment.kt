package com.androidvibes.mindbodytest.ui.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.androidvibes.mindbodytest.R
import com.androidvibes.mindbodytest.data.models.ApiCallStates
import com.androidvibes.mindbodytest.ui.activities.MainActivity
import com.androidvibes.mindbodytest.ui.components.CountryListContent
import com.androidvibes.mindbodytest.ui.components.CustomShimmer
import com.androidvibes.mindbodytest.ui.components.ErrorScreen
import com.androidvibes.mindbodytest.utils.addProvincesListFragmentFragment
import com.androidvibes.mindbodytest.viewmodel.CountryViewModel
import kotlinx.coroutines.Job
import java.lang.ref.WeakReference


class CountryListFragment : Fragment() {

    private var classTag = CountryListFragment::class.java.simpleName
    private var activityWeakReference: WeakReference<MainActivity?>? = null
    private val viewModel: CountryViewModel by activityViewModels()
    private lateinit var apiJob: Job


    private fun getActivityWeakReference(): MainActivity? {
        return try {
            activityWeakReference = if (activityWeakReference == null)
                WeakReference<MainActivity?>(requireActivity() as MainActivity?)
            else activityWeakReference
            activityWeakReference?.get()
        } catch (ex: Exception) {
            Log.d(
                classTag,
                "Catch block of getActivityWeakReference method ${ex.localizedMessage}"
            )
            ex.printStackTrace()
            null
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getActivityWeakReference()?.setScreenName("Country Screen")
        getActivityWeakReference()?.showHideBackButton(false)
        return ComposeView(requireContext()).apply {
            setContent {
                val apiState = viewModel.uiState.collectAsState().value
                ManageUiStates(apiCallStates = apiState)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        viewModel.saveCountryListState()
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            viewModel.restoreCountryListState(savedInstanceState)
        }
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.countryScreenList.isNullOrEmpty()) {
            apiJob = viewModel.fetchCountries()
        } else {
            viewModel.setCountryList(viewModel.countryScreenList ?: emptyList())
        }
    }

    override fun onDestroyView() {
        if (::apiJob.isInitialized) {
            apiJob.cancel()
        }
        super.onDestroyView()
    }

    @Composable
    fun ManageUiStates(apiCallStates: ApiCallStates) {
        when (apiCallStates) {
            is ApiCallStates.ApiError -> {
                ErrorScreen(
                    errorIc = R.drawable.ic_error_cloud,
                    errorMessage = apiCallStates.message
                        ?: stringResource(R.string.generic_error_msg),
                    onRetryClick = {
                        viewModel.fetchCountries()
                    }
                )
            }
            is ApiCallStates.CountryListSuccess -> {
                if (apiCallStates.countryList.isNotEmpty()) {
                    CountryListContent(apiCallStates.countryList,
                        onItemClick = { countryCode ->
                            viewModel.setCountryCode(countryCode)
                            getActivityWeakReference()?.addProvincesListFragmentFragment(R.id.fragment_container)
                        })
                } else {
                    ErrorScreen(
                        errorIc = R.drawable.ic_error_cloud,
                        errorMessage = stringResource(R.string.no_data_msg),
                        onRetryClick = {
                            viewModel.fetchCountries()
                        }
                    )
                }
            }
            ApiCallStates.InProgress -> {
                CustomShimmer()
            }
            is ApiCallStates.NetworkError -> {
                ErrorScreen(
                    errorIc = R.drawable.ic_error_network,
                    errorMessage = apiCallStates.message
                        ?: stringResource(id = R.string.no_internet_message_text),
                    onRetryClick = {
                        viewModel.fetchCountries()
                    }
                )
            }
            is ApiCallStates.ProvincesListSuccess -> {
            }
        }
    }
}