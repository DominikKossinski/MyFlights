package pl.kossa.myflights.architecture

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import okhttp3.internal.http2.ConnectionShutdownException
import pl.kossa.myflights.MainNavGraphDirections
import pl.kossa.myflights.R
import pl.kossa.myflights.api.exceptions.NoInternetException
import pl.kossa.myflights.api.responses.ApiError
import pl.kossa.myflights.api.responses.ApiErrorBody
import pl.kossa.myflights.api.responses.HttpCode
import pl.kossa.myflights.livedata.SingleLiveEvent
import pl.kossa.myflights.utils.PreferencesHelper
import retrofit2.Converter
import retrofit2.Response
import java.io.IOException
import java.lang.Exception
import java.net.SocketTimeoutException
import java.net.UnknownHostException

abstract class BaseViewModel(
    private val errorBodyConverter: Converter<ResponseBody, ApiErrorBody>,
    private val preferencesHelper: PreferencesHelper
) : ViewModel() {

    protected val firebaseAuth = FirebaseAuth.getInstance()

    private var tokenRefreshed = false

    val navDirectionLiveData = SingleLiveEvent<NavDirections>()
    val backLiveData = SingleLiveEvent<Unit>()
    val signOutLiveData = SingleLiveEvent<Unit>()

    val toastError = MutableSharedFlow<Int?>(0)
    val isLoadingData = MutableStateFlow(false)
    val apiErrorFlow = MutableStateFlow<ApiError?>(null)


    fun <T> makeRequest(request: suspend () -> Response<T>, onSuccess: (T) -> Unit) {
        viewModelScope.launch {
            isLoadingData.value = true
            try {

                val response = request.invoke()
                Log.d("MyLog", "Response $response")
                if (response.isSuccessful) {
                    onSuccess.invoke(response.body()!!)
                } else {
                    if (response.code() == HttpCode.UNAUTHORIZED.code) {
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
                        convertErrorBody(response.code(), response.errorBody())
                    }

                }
                isLoadingData.value = false
            } catch (e: NoInternetException) {
                isLoadingData.value = false
                setToastError(R.string.error_no_internet)
            } catch (e: Exception) {
                isLoadingData.value = false
                when (e) {
                    is SocketTimeoutException, is UnknownHostException, is ConnectionShutdownException, is IOException -> {
                        toastError.emit(R.string.error_no_connection_to_server)
                    }
                    else -> {
                        setToastError(R.string.unexpected_error)
                    }
                }
            }
        }
    }

    fun makeRequest(request: suspend () -> Response<Void>, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoadingData.value = true
            try {
                val response = request.invoke()
                Log.d("MyLog", "Response $response")
                if (response.isSuccessful) {
                    onSuccess.invoke()
                } else {
                    if (response.code() == HttpCode.UNAUTHORIZED.code) {
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
                        convertErrorBody(response.code(), response.errorBody())
                    }

                }
                isLoadingData.value = false
            } catch (e: NoInternetException) {
                isLoadingData.value = false
                setToastError(R.string.error_no_internet)
            } catch (e: SocketTimeoutException) {
                isLoadingData.value = false
                setToastError(R.string.error_no_connection_to_server)
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
                    setToastError(R.string.error_no_internet)
                }
                else -> {
                    Log.d("MyLog", "Token error: $it")
                    setToastError(R.string.unexpected_error)
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

    private fun convertErrorBody(code: Int, errorBody: ResponseBody?) {
        val apiError = try {
            val apiErrorBody = errorBody?.let { errorBodyConverter.convert(it) }
            ApiError(code, apiErrorBody)
        } catch (e: IOException) {
            ApiError(HttpCode.INTERNAL_SERVER_ERROR.code, null)
        }
        apiErrorFlow.value = apiError
    }

    fun setToastError(stringId: Int?) {
        viewModelScope.launch {
            toastError.emit(stringId)
        }
    }
}