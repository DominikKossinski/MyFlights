package pl.kossa.myflights.architecture

import android.util.Log
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import pl.kossa.myflights.MainNavGraphDirections
import pl.kossa.myflights.R
import pl.kossa.myflights.api.ApiService
import pl.kossa.myflights.utils.PreferencesHelper
import retrofit2.Response

abstract class BaseViewModel(
    val navController: NavController,
    private val preferencesHelper: PreferencesHelper
) : ViewModel(), Observable {

    protected val firebaseAuth = FirebaseAuth.getInstance()

    private var tokenRefreshed = true

    protected val apiService by lazy {
        ApiService(preferencesHelper)
    }
    val toastError = MutableLiveData<Int?>(null)
    val isLoadingData = MutableLiveData(false)

    private val callbacks: PropertyChangeRegistry = PropertyChangeRegistry()

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        callbacks.add(callback)
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        callbacks.remove(callback)
    }

    fun notifyChange() {
        callbacks.notifyCallbacks(this, 0, null)
    }

    fun notifyPropertyChanged(fieldId: Int) {
        callbacks.notifyCallbacks(this, fieldId, null)
    }

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
                        goToLoginActivity()
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
                    goToLoginActivity()
                }
            }
            isLoadingData.value = false
        }
    }

    private fun goToLoginActivity() {

        when (navController.graph.id) {
            R.id.main_nav_graph -> {
                navController.navigate(MainNavGraphDirections.goToLoginActivity())
            }
        }
    }
}