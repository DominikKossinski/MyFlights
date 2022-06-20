package pl.kossa.myflights.repository

import pl.kossa.myflights.api.call.ApiResponse1
import pl.kossa.myflights.api.exceptions.ApiServerException
import pl.kossa.myflights.api.exceptions.NoInternetException
import pl.kossa.myflights.api.exceptions.UnauthorizedException
import pl.kossa.myflights.api.requests.AirplaneRequest
import pl.kossa.myflights.api.responses.ApiError
import pl.kossa.myflights.api.responses.ApiErrorBody
import pl.kossa.myflights.api.services.AirplanesService
import pl.kossa.myflights.room.dao.AirplaneDao
import pl.kossa.myflights.room.entities.Airplane
import pl.kossa.myflights.utils.PreferencesHelper

sealed class ResultWrapper<out T>(val value: T) {

    class Success<out T>(value: T) : ResultWrapper<T>(value)

    class GenericError<out T>(value: T, val apiError: ApiError) : ResultWrapper<T>(value)
}

class AirplaneRepository(
    private val airplanesService: AirplanesService,
    private val airplaneDao: AirplaneDao,
    private val preferencesHelper: PreferencesHelper
) {

    suspend fun <T> makeRequest(
        tokenRefreshed: Boolean,
        block: suspend () -> ApiResponse1<T>
    ): ApiResponse1<T> {
        return try {
            block.invoke()
        } catch (e: UnauthorizedException) {

            if (tokenRefreshed) {
                // TODO sign out
                //firebaseAuth.signOut()
                //signOutFlow.emit(Unit)

            } else {
                //tokenRefreshed = true
                // TODO
//                    refreshToken {
//                        makeRequest(block)
//                    }
            }
            return ApiResponse1.GenericError(ApiError(401, ApiErrorBody("mess", "desc")))
        } catch (e: NoInternetException) {
            return ApiResponse1.GenericError(ApiError(500, ApiErrorBody("mess", "desc")))
            // TODO emit error setToastMessage(R.string.error_no_internet)
        } catch (e: ApiServerException) {
            return ApiResponse1.GenericError(ApiError(401, ApiErrorBody("mess", "desc")))
            // TODO emit error apiErrorFlow.emit(e.apiError)
        } catch (e: Exception) {
            return ApiResponse1.GenericError(ApiError(404, ApiErrorBody("mess", "desc")))
//                when (e) {
//                    is SocketTimeoutException, is UnknownHostException, is ConnectionShutdownException, is IOException -> {
//                        e.printStackTrace()
//                        return ApiResponse1.GenericError(ApiError(401, ApiErrorBody("mess", "desc")))
//                        // TODO emit error  toastMessage.emit(R.string.error_no_connection_to_server)
//                    }
//                    else -> {
//                        return ApiResponse1.GenericError(ApiError(401, ApiErrorBody("mess", "desc")))
//                        e.printStackTrace()
//                        // TODO emit error setToastMessage(R.string.unexpected_error)
//                    }
//                }
        }
    }

    suspend fun getAirplanes(): ResultWrapper<List<Airplane>> {
        val response = makeRequest(true, airplanesService::getAirplanes) /*{
            val response = airplanesService.getAirplanes()
            val airplanes = when (response) {
                is ApiResponse1.Success -> response.value ?: emptyList()
                else -> emptyList()
            }//response.value ?: emptyList()

        }*/
        val airplanes = when (response) {
            is ApiResponse1.Success -> response.value ?: emptyList()
            else -> emptyList()
        }//response.value ?: emptyList()
        airplanes.forEach {
            airplaneDao.insertAirplane(Airplane.fromApiAirplane(it))
        }
        val value = preferencesHelper.userId?.let { airplaneDao.getAll(it) } ?: emptyList()
        return when (response) {
            is ApiResponse1.Success -> ResultWrapper.Success(value)
            is ApiResponse1.GenericError -> ResultWrapper.GenericError(value, response.apiError)
        }
    }

    suspend fun getAirplaneById(airplaneId: String): Airplane? {
        val response = airplanesService.getAirplaneById(airplaneId)
        response.body?.let {
            airplaneDao.insertAirplane(Airplane.fromApiAirplane(it))
        }
        return preferencesHelper.userId?.let { airplaneDao.getAirplaneById(it, airplaneId) }
    }

    suspend fun createAirplane(airplaneRequest: AirplaneRequest): String? {
        val response = airplanesService.postAirplane(airplaneRequest)
        response.body?.entityId?.let {
            getAirplaneById(it)
        }
        return response.body?.entityId
    }

    suspend fun saveAirplane(airplaneId: String, airplaneRequest: AirplaneRequest) {
        airplanesService.putAirplane(airplaneId, airplaneRequest)
        getAirplaneById(airplaneId)
    }

    suspend fun deleteAirplane(airplaneId: String) {
        airplanesService.deleteAirplane(airplaneId)
        preferencesHelper.userId?.let {
            val airplane = airplaneDao.getAirplaneById(it, airplaneId)
            airplane?.let { entity ->
                airplaneDao.delete(entity)
            }
        }
    }
}