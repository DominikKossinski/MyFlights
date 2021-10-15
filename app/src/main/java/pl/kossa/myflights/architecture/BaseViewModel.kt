package pl.kossa.myflights.architecture

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import pl.kossa.myflights.MainNavGraphDirections
import pl.kossa.myflights.R
import pl.kossa.myflights.livedata.SingleLiveEvent
import pl.kossa.myflights.utils.PreferencesHelper
import retrofit2.Response

abstract class BaseViewModel(
    private val preferencesHelper: PreferencesHelper
) : ViewModel() {

    protected val firebaseAuth = FirebaseAuth.getInstance()

    private var tokenRefreshed = false

    val toastError = MutableLiveData<Int?>(null)
    val isLoadingData = MutableLiveData(false)
    val navDirectionLiveData = SingleLiveEvent<NavDirections>()
    val backLiveData = SingleLiveEvent<Unit>()
    val signOutLiveData = SingleLiveEvent<Unit>()


    fun <T> makeRequest(request: suspend () -> Response<T>, onSuccess: (T) -> Unit) {
        viewModelScope.launch {
            isLoadingData.value = true
            val response = request.invoke()
            Log.d("MyLog", "Response $response")
            if (response.isSuccessful) {
                onSuccess.invoke(response.body()!!)
            } else {
                if (response.code() == 401) {
                    if (tokenRefreshed) {
                        firebaseAuth.signOut()
                        signOutLiveData.value = Unit
                    } else {
                        tokenRefreshed = true
                        refreshToken {
                            makeRequest(request, onSuccess)
                        }
                    }
                } else {
                    Log.d("MyLog", "Error $response")
                    TODO("handle errors")
                }

            }
            isLoadingData.value = false
            Log.d("MyLog", "false")
        }
    }

    fun makeRequest(request: suspend () -> Response<Void>, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoadingData.value = true
            val response = request.invoke()
            Log.d("MyLog", "Response $response")
            if (response.isSuccessful) {
                onSuccess.invoke()
            } else {
                if (response.code() == 401) {
                    if (tokenRefreshed) {
                        firebaseAuth.signOut()
                        signOutLiveData.value = Unit
                    } else {
                        tokenRefreshed = true
                        refreshToken {
                            makeRequest(request, onSuccess)
                        }
                    }
                } else {
                    Log.d("MyLog", "Error $response")
                    TODO("handle errors")
                }

            }
            isLoadingData.value = false
            Log.d("MyLog", "false")
        }
    }

    protected fun refreshToken(onSuccess: () -> Unit) {
        firebaseAuth.currentUser?.getIdToken(true)?.addOnSuccessListener {
            preferencesHelper.token = it.token
            onSuccess()
        }?.addOnFailureListener {
            when (it) {
                is FirebaseNetworkException -> {
                    toastError.value = R.string.no_internet_error
                }
                else -> {
                    Log.d("MyLog", "Token error: $it")
                    toastError.value = R.string.unexpected_error
                    firebaseAuth.signOut()
                    preferencesHelper.token = null
                    signOutLiveData.value = Unit
                }
            }
            isLoadingData.value = false
        }
    }

    fun showComingSoonDialog() {
        navDirectionLiveData.value = MainNavGraphDirections.showComingSoonDialog()
    }

    fun navigateBack() {
        backLiveData.value = Unit
    }

    fun signOut() {
        firebaseAuth.signOut()
        preferencesHelper.token = null
        signOutLiveData.value = Unit
    }
}