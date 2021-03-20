package pl.kossa.myflights.fragments.emailresend

import com.google.firebase.FirebaseNetworkException
import pl.kossa.myflights.R
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper

class EmailResendViewModel(
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {


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
        firebaseAuth.signOut()
        navDirectionLiveData.value = EmailResendFragmentDirections.goToLoginFragment()
    }

}