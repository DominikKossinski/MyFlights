package pl.kossa.myflights.fragments.profile.settings.email.verify

import com.google.firebase.FirebaseNetworkException
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.ResponseBody
import pl.kossa.myflights.R
import pl.kossa.myflights.api.responses.ApiErrorBody
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import retrofit2.Converter
import javax.inject.Inject

@HiltViewModel
class EmailVerifyViewModel @Inject constructor(
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    fun resendEmail() {
        firebaseAuth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
            setToastMessage(R.string.email_resended)
        }?.addOnFailureListener {
            when (it) {
                is FirebaseNetworkException -> setToastMessage(R.string.error_no_internet)
                else -> setToastMessage(R.string.unexpected_error)
            }
        }
    }

    fun navigateToLogin() {
        signOut()
    }
}