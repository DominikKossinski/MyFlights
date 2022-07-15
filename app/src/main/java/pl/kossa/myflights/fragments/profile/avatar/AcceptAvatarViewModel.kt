package pl.kossa.myflights.fragments.profile.avatar

import android.content.Context
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import pl.kossa.myflights.api.models.User
import pl.kossa.myflights.api.requests.UserRequest
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.repository.ImageRepository
import pl.kossa.myflights.repository.UserRepository
import pl.kossa.myflights.utils.PreferencesHelper
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AcceptAvatarViewModel @Inject constructor(
    private val imageRepository: ImageRepository,
    private val userRepository: UserRepository,
    savedStateHandle: SavedStateHandle,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    val imageUri = Uri.parse(savedStateHandle.get<String>("imageUri")!!)!!
    val _user = MutableStateFlow<User?>(null)
    private val _filePath = MutableStateFlow<String?>(null)
    val isSaveButtonEnabled = combine(_user, _filePath) { user, filePath ->
        return@combine user != null && filePath != null
    }

    init {
        fetchUser()
    }

    fun fetchUser() {
        makeRequest {
            val response = handleRequest {
                userRepository.getUser()
            }
            _user.emit(response)
        }
    }

    fun saveAvatar(context: Context) {
        viewModelScope.launch {
            val imageId = _user.value?.avatar?.imageId
            val nick = _user.value?.nick ?: ""
            val regulationsAccepted = _user.value?.regulationsAccepted ?: false
            _filePath.value?.let {
                val file = File(it)
                val type = context.contentResolver.getType(imageUri)!!
                val part = MultipartBody.Part.createFormData(
                    "image",
                    file.name,
                    file.asRequestBody(type.toMediaTypeOrNull())
                )
                if (imageId != null) {
                    imageRepository.putImage(imageId, part)
                } else {
                    val entityId = handleRequest {
                        imageRepository.postImage(part)
                    }
                    if (entityId != null) {
                        handleRequest {
                            userRepository.putUser(
                                UserRequest(
                                    nick,
                                    entityId,
                                    regulationsAccepted
                                )
                            )
                        }
                    }
                }
                analyticsTracker.logClickSaveAvatar()
                navigateBack()
            }
        }
    }

    fun setFilePath(filePath: String) {
        _filePath.value = filePath
    }

}