package pl.kossa.myflights.activities.main

import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.ResponseBody
import pl.kossa.myflights.api.responses.ApiErrorBody
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import retrofit2.Converter
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

}
