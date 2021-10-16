package pl.kossa.myflights.fragments.prelogin

import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.ResponseBody
import pl.kossa.myflights.api.responses.ApiErrorBody
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import retrofit2.Converter
import javax.inject.Inject

@HiltViewModel
class PreLoginViewModel @Inject constructor(
    errorBodyConverter: Converter<ResponseBody, ApiErrorBody>,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(errorBodyConverter, preferencesHelper) {

    fun navigateToLogin() {
        navDirectionLiveData.value = PreLoginFragmentDirections.goToLoginFragment()
    }

    fun navigateToCrateAccount() {
        navDirectionLiveData.value = PreLoginFragmentDirections.goToCreateAccount()
    }
}