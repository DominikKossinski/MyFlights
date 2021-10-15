package pl.kossa.myflights.fragments.profile

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.ResponseBody
import pl.kossa.myflights.api.models.User
import pl.kossa.myflights.api.responses.ApiErrorBody
import pl.kossa.myflights.api.services.UserService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.fragments.main.MainFragmentDirections
import pl.kossa.myflights.utils.PreferencesHelper
import retrofit2.Converter
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userService: UserService,
    errorBodyConverter: Converter<ResponseBody, ApiErrorBody>,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(errorBodyConverter, preferencesHelper) {

    val user = MutableStateFlow<User?>(null)

    init {
        fetchUser()
    }

    fun fetchUser() {
        makeRequest(userService::getUser) { response ->
            user.value = response
        }
    }

    fun navigateToSettings() {
        navDirectionLiveData.value = MainFragmentDirections.goToSettings()
    }
}