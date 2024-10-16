package pl.kossa.myflights.fragments.emailresend

import com.google.firebase.FirebaseNetworkException
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.kossa.myflights.R
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class EmailResendViewModel @Inject constructor(
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
        firebaseAuth.signOut()
        navigate(EmailResendFragmentDirections.goToLoginFragment())
    }

}