package com.androidvibes.mindbodytest.ui.fragments

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.androidvibes.mindbodytest.R
import com.androidvibes.mindbodytest.data.models.ApiCallStates
import com.androidvibes.mindbodytest.ui.activities.MainActivity
import com.androidvibes.mindbodytest.ui.components.CustomShimmer
import com.androidvibes.mindbodytest.ui.components.ErrorScreen
import com.androidvibes.mindbodytest.ui.components.ProvincesListContent
import com.androidvibes.mindbodytest.utils.removeProvincesListFragmentFragment
import com.androidvibes.mindbodytest.viewmodel.CountryViewModel
import kotlinx.coroutines.Job
import java.lang.ref.WeakReference

class ProvincesListFragment : Fragment() {

    private var classTag = ProvincesListFragment::class.java.simpleName
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getActivityWeakReference()?.onBackPressedDispatcher?.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    getActivityWeakReference()?.removeProvincesListFragmentFragment()
                }
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.provinceScreenList.isNullOrEmpty()
            || (getActivityWeakReference()?.selectedCountryCode != viewModel.countryCode.value)) {
            getActivityWeakReference()?.selectedCountryCode = viewModel.countryCode.value
            apiJob = viewModel.fetchProvinces(viewModel.countryCode.value)
        } else {
            viewModel.setProvincesList(viewModel.provinceScreenList ?: emptyList())
        }
    }

    override fun onDestroyView() {
        if (::apiJob.isInitialized) {
            apiJob.cancel()
        }
        super.onDestroyView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        viewModel.saveProvincesListState()
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            viewModel.restoreProvincesListState(savedInstanceState)
        }
        super.onViewStateRestored(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        getActivityWeakReference()?.setScreenName("Provinces Screen")
        getActivityWeakReference()?.showHideBackButton(true)
        return ComposeView(requireContext()).apply {
            setContent {
                val apiState = viewModel.uiState.collectAsState().value
                ManageUiStates(apiCallStates = apiState)
            }
        }
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
                        viewModel.fetchProvinces(viewModel.countryCode.value)
                    }
                )
            }
            is ApiCallStates.CountryListSuccess -> {
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
                        viewModel.fetchProvinces(viewModel.countryCode.value)
                    }
                )
            }
            is ApiCallStates.ProvincesListSuccess -> {
                if (apiCallStates.provincesList.isNotEmpty()) {
                    ProvincesListContent(provincesList = apiCallStates.provincesList)
                } else {
                    ErrorScreen(
                        errorIc = R.drawable.ic_error_cloud,
                        errorMessage = stringResource(R.string.no_data_msg),
                        onRetryClick = {
                            viewModel.fetchProvinces(viewModel.countryCode.value)
                        }
                    )
                }
            }
        }
    }
}