package pl.kossa.myflights.fragments.profile.settings.email.verify

import com.google.firebase.FirebaseNetworkException
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.kossa.myflights.R
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.fragments.emailresend.EmailResendFragmentDirections
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class EmailVerifyViewModel @Inject constructor(
    preferencesHelper: PreferencesHelper
): BaseViewModel(preferencesHelper) {

    fun resendEmail() {
        firebaseAuth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
            toastError.value = R.string.email_resended
        }?.addOnFailureListener {
            when (it) {
                is FirebaseNetworkException -> toastError.value =
                    R.string.no_internet_error
                else -> {
                    toastError.value = R.string.unexpected_error
                }
            }
        }
    }

    fun navigateToLogin() {
        signOut()
    }
}