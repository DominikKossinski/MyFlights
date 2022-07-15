package pl.kossa.myflights.repository

import okhttp3.MultipartBody
import pl.kossa.myflights.api.call.ApiResponse1
import pl.kossa.myflights.api.services.ImagesService
import pl.kossa.myflights.architecture.BaseRepository
import pl.kossa.myflights.architecture.ResultWrapper
import pl.kossa.myflights.utils.PreferencesHelper

class ImageRepository(
    private val imagesService: ImagesService,
    preferencesHelper: PreferencesHelper
) : BaseRepository(preferencesHelper) {

    suspend fun postImage(image: MultipartBody.Part): ResultWrapper<String?> {
        val response = makeRequest {
            imagesService.postImage(image)
        }
        return when (response) {
            is ApiResponse1.Success -> ResultWrapper.Success(response.value?.entityId)
            is ApiResponse1.GenericError -> ResultWrapper.GenericError(null, response.apiError)
            is ApiResponse1.NetworkError -> ResultWrapper.NetworkError(
                null,
                response.networkErrorType
            )
        }
    }

    suspend fun putImage(imageId: String, image: MultipartBody.Part): ResultWrapper<Unit?> {
        val response = makeRequest {
            imagesService.putImage(imageId, image)
        }
        return when (response) {
            is ApiResponse1.Success -> ResultWrapper.Success(Unit)
            is ApiResponse1.GenericError -> ResultWrapper.GenericError(null, response.apiError)
            is ApiResponse1.NetworkError -> ResultWrapper.NetworkError(
                null,
                response.networkErrorType
            )
        }
    }

}