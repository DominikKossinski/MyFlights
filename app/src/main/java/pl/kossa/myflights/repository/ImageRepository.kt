package pl.kossa.myflights.repository

import okhttp3.MultipartBody
import pl.kossa.myflights.api.services.ImagesService

class ImageRepository(
    private val imagesService: ImagesService
) {

    suspend fun postImage(image: MultipartBody.Part) = imagesService.postImage(image)

    suspend fun putImage(imageId: String, image: MultipartBody.Part) =
        imagesService.putImage(imageId, image)

}