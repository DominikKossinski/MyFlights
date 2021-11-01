package pl.kossa.myflights.api.services

import okhttp3.MultipartBody
import pl.kossa.myflights.api.call.ApiResponse
import pl.kossa.myflights.api.responses.CreatedResponse
import retrofit2.http.*

interface ImagesService {

    @Multipart
    @POST("/api/images")
    suspend fun postImage(@Part image: MultipartBody.Part): ApiResponse<CreatedResponse>

    @Multipart
    @PUT("/api/images/{imageId}")
    suspend fun putImage(
        @Path("imageId") imageId: String,
        @Part image: MultipartBody.Part
    ): ApiResponse<CreatedResponse>


}