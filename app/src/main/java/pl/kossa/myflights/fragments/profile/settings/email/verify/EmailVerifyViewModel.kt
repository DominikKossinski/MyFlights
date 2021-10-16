package pl.kossa.myflights.fragments.profile.settings.email.verify

import com.google.firebase.FirebaseNetworkException
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.ResponseBody
import pl.kossa.myflights.R
import pl.kossa.myflights.api.responses.ApiErrorBody
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.fragments.emailresend.EmailResendFragmentDirections
import pl.kossa.myflights.utils.PreferencesHelper
import retrofit2.Converter
import javax.inject.Inject

@HiltViewModel
class EmailVerifyViewModel @Inject constructor(
    errorBodyConverter: Converter<ResponseBody, ApiErrorBody>,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(errorBodyConverter, preferencesHelper) {

    fun resendEmail() {
        firebaseAuth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
            setToastError(R.string.email_resended)
        }?.addOnFailureListener {
            when (it) {
                is FirebaseNetworkException -> setToastError(R.string.error_no_internet)
                else -> setToastError(R.string.unexpected_error)
            }
        }
    }

    fun navigateToLogin() {
        signOut()
    }
}