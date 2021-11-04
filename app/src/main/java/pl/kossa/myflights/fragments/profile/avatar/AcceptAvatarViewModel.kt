package pl.kossa.myflights.fragments.profile.avatar

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pl.kossa.myflights.api.models.User
import pl.kossa.myflights.api.requests.UserRequest
import pl.kossa.myflights.api.services.ImagesService
import pl.kossa.myflights.api.services.UserService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AcceptAvatarViewModel @Inject constructor(
    private val imagesService: ImagesService,
    private val userService: UserService,
    savedStateHandle: SavedStateHandle,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    val imageUri = Uri.parse(savedStateHandle.get<String>("imageUri")!!)!!
    val _user = MutableStateFlow<User?>(null)
    private val _filePath = MutableStateFlow<String?>(null)
    val isSaveButtonEnabled = combine(_user, _filePath) {
        user, filePath ->
        return@combine user != null && filePath != null
    }

    init {
        fetchUser()
    }

    fun fetchUser() {
        makeRequest {
            val response = userService.getUser()
            _user.emit(response.body)
        }
    }

    fun saveAvatar(context: Context) {
        viewModelScope.launch {
            val imageId = _user.value?.avatar?.imageId
            _filePath.value?.let {
                val file = File(it)
                val type = context.contentResolver.getType(imageUri)!!
                val part = MultipartBody.Part.createFormData("image", file.name,RequestBody.create(type.toMediaTypeOrNull()!!, file))
                if(imageId != null) {
                    imagesService.putImage(imageId, part)
                } else {
                    val response = imagesService.postImage(part)
                    userService.putUser(UserRequest("Nick", response.body!!.entityId))
                }
                navigateBack()
            }
        }
    }

    fun setFilePath(filePath: String) {
        _filePath.value = filePath
    }

}