package pl.kossa.myflights.fragments.launcher

import android.util.Log
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class LauncherViewModel @Inject constructor(preferencesHelper: PreferencesHelper) :
    BaseViewModel(preferencesHelper) {

    fun openNextActivity() {
        Log.d("MyLog", "Current user: $currentUser")
        if (currentUser == null || !currentUser.isEmailVerified) {
            firebaseAuth.signOut()
            navigate(LauncherFragmentDirections.goToPreLogin())
        } else {
            navigate(LauncherFragmentDirections.goToMainActivity(), true)
        }
    }
}