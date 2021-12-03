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
import okhttp3.internal.http2.ConnectionShutdownException
import pl.kossa.myflights.MainNavGraphDirections
import pl.kossa.myflights.R
import pl.kossa.myflights.analytics.AnalyticsTracker
import pl.kossa.myflights.api.exceptions.ApiServerException
import pl.kossa.myflights.api.exceptions.NoInternetException
import pl.kossa.myflights.api.exceptions.UnauthorizedException
import pl.kossa.myflights.api.responses.ApiError
import pl.kossa.myflights.utils.PreferencesHelper
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

abstract class BaseViewModel(
    private val preferencesHelper: PreferencesHelper
) : ViewModel() {


    @Inject
    protected lateinit var analyticsTracker: AnalyticsTracker

    protected val firebaseAuth = FirebaseAuth.getInstance()
    protected val currentUser = firebaseAuth.currentUser

    private var tokenRefreshed = false

    val toastMessage = MutableSharedFlow<Int?>(0)
    val isLoadingData = MutableStateFlow(false)
    val apiErrorFlow = MutableStateFlow<ApiError?>(null)
    val activityFinishFlow = MutableSharedFlow<Unit>(0)
    private val navDirectionFlow = MutableSharedFlow<NavDirections>(0)
    val backFlow = MutableSharedFlow<Unit>(0)
    val signOutFlow = MutableSharedFlow<Unit>(0)


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
                    signOutFlow.emit(Unit)
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
                    viewModelScope.launch {
                        signOutFlow.emit(Unit)
                    }
                }
            }
            isLoadingData.value = false
        }
    }

    fun showComingSoonDialog() {
        viewModelScope.launch {
            navigate(MainNavGraphDirections.showComingSoonDialog())
        }
    }

    protected fun navigate(action: NavDirections, finishActivity: Boolean = false) {
        viewModelScope.launch {
            Log.d("MyLog", "${this@BaseViewModel::class.java} Emitting: $action")
            navDirectionFlow.emit(action)
            if(finishActivity) {
                activityFinishFlow.emit(Unit)
            }
        }
    }

    fun navigateBack() {
        viewModelScope.launch {
            backFlow.emit(Unit)
        }
    }

    fun signOut() {
        firebaseAuth.signOut()
        analyticsTracker.setUserId(null)
        preferencesHelper.token = null
        viewModelScope.launch {
            signOutFlow.emit(Unit)
        }
    }

    fun setToastMessage(stringId: Int?) {
        viewModelScope.launch {
            toastMessage.emit(stringId)
        }
    }

    fun getNavDirectionsFlow() = navDirectionFlow
}