package pl.kossa.myflights.dialogs.coming

import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.ResponseBody
import pl.kossa.myflights.api.responses.ApiError
import pl.kossa.myflights.api.responses.ApiErrorBody
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import retrofit2.Converter
import javax.inject.Inject

@HiltViewModel
class ComingSoonViewModel @Inject constructor(
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {
}