package pl.kossa.myflights.architecture

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import okhttp3.internal.http2.ConnectionShutdownException
import pl.kossa.myflights.MainNavGraphDirections
import pl.kossa.myflights.R
import pl.kossa.myflights.api.call.ApiResponse
import pl.kossa.myflights.api.exceptions.ApiServerException
import pl.kossa.myflights.api.exceptions.NoInternetException
import pl.kossa.myflights.api.exceptions.UnauthorizedException
import pl.kossa.myflights.api.responses.ApiError
import pl.kossa.myflights.api.responses.ApiErrorBody
import pl.kossa.myflights.api.responses.HttpCode
import pl.kossa.myflights.livedata.SingleLiveEvent
import pl.kossa.myflights.utils.PreferencesHelper
import retrofit2.Converter
import java.io.IOException
import java.lang.Exception
import java.net.SocketTimeoutException
import java.net.UnknownHostException

abstract class BaseViewModel(
    private val preferencesHelper: PreferencesHelper
) : ViewModel() {

    protected val firebaseAuth = FirebaseAuth.getInstance()

    private var tokenRefreshed = false

    val navDirectionLiveData = SingleLiveEvent<NavDirections>()
    val backLiveData = SingleLiveEvent<Unit>()
    val signOutLiveData = SingleLiveEvent<Unit>()

    val toastMessage = MutableSharedFlow<Int?>(0)
    val isLoadingData = MutableStateFlow(false)
    val apiErrorFlow = MutableStateFlow<ApiError?>(null)

    fun makeRequest(block: suspend () -> Unit) {
        viewModelScope.launch {
            isLoadingData.value = true
            try {
                block.invoke()
            } catch (e: ApiServerException) {
                apiErrorFlow.emit(e.apiError)
            } catch (e: UnauthorizedException) {
                if (tokenRefreshed) {
                    firebaseAuth.signOut()
                    signOutLiveData.value = Unit
                } else {
                    tokenRefreshed = true
                    refreshToken {
                        makeRequest(block)
                    }
                }
            } catch (e: NoInternetException) {
                setToastMessage(R.string.error_no_internet)
            } catch (e: Exception) {
                when (e) {
                    is SocketTimeoutException, is UnknownHostException, is ConnectionShutdownException, is IOException -> {
                        e.printStackTrace()
                        toastMessage.emit(R.string.error_no_connection_to_server)
                    }
                    else -> {
                        setToastMessage(R.string.unexpected_error)
                    }
                }
            } finally {
                isLoadingData.value = false
            }
        }
    }


    protected fun refreshToken(onSuccess: () -> Unit) {
        firebaseAuth.currentUser?.getIdToken(true)?.addOnSuccessListener {
            preferencesHelper.token = it.token
            onSuccess()
        }?.addOnFailureListener {
            when (it) {
                is FirebaseNetworkException -> {
                    setToastMessage(R.string.error_no_internet)
                }
                else -> {
                    Log.d("MyLog", "Token error: $it")
                    setToastMessage(R.string.unexpected_error)
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

    fun setToastMessage(stringId: Int?) {
        viewModelScope.launch {
            toastMessage.emit(stringId)
        }
    }
}