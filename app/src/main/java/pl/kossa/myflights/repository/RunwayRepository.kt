package pl.kossa.myflights.repository

import pl.kossa.myflights.api.call.ApiResponse1
import pl.kossa.myflights.api.requests.RunwayRequest
import pl.kossa.myflights.api.responses.HttpCode
import pl.kossa.myflights.api.services.RunwaysService
import pl.kossa.myflights.architecture.BaseRepository
import pl.kossa.myflights.architecture.ResultWrapper
import pl.kossa.myflights.room.dao.RunwayDao
import pl.kossa.myflights.room.entities.Runway
import pl.kossa.myflights.utils.PreferencesHelper

class RunwayRepository(
    private val runwaysService: RunwaysService,
    private val runwayDao: RunwayDao,
    preferencesHelper: PreferencesHelper
) : BaseRepository(preferencesHelper) {

    suspend fun getRunwayById(airportId: String, runwayId: String): ResultWrapper<Runway?> {
        val response = makeRequest {
            runwaysService.getRunwayById(airportId, runwayId)
        }
        return when (response) {
            is ApiResponse1.Success -> {
                response.value?.let {
                    runwayDao.insertRunway(Runway.fromApiRunway(airportId, it))
                }
                ResultWrapper.Success(preferencesHelper.userId?.let {
                    runwayDao.getRunwayById(
                        it, airportId, runwayId
                    )
                })
            }
            is ApiResponse1.GenericError -> {
                when (response.apiError.code) {
                    HttpCode.NOT_FOUND.code -> {
                        val runway = preferencesHelper.userId?.let {
                            runwayDao.getRunwayById(
                                it, airportId, runwayId
                            )
                        }
                        runway?.let { runwayDao.delete(runway) }
                        ResultWrapper.GenericError(null, response.apiError)
                    }
                    else -> {
                        val runway = preferencesHelper.userId?.let {
                            runwayDao.getRunwayById(
                                it, airportId, runwayId
                            )
                        }
                        ResultWrapper.GenericError(runway, response.apiError)
                    }
                }
            }
            is ApiResponse1.NetworkError -> {
                ResultWrapper.NetworkError(preferencesHelper.userId?.let {
                    runwayDao.getRunwayById(
                        it, airportId, runwayId
                    )
                }, response.networkErrorType)
            }
        }
    }

    suspend fun createRunway(airportId: String, runwayRequest: RunwayRequest): String? {
        val response = runwaysService.postRunway(airportId, runwayRequest)
        response.body?.entityId?.let {
            getRunwayById(airportId, it)
        }
        return response.body?.entityId
    }

    suspend fun savaRunway(airportId: String, runwayId: String, runwayRequest: RunwayRequest) {
        runwaysService.putRunway(airportId, runwayId, runwayRequest)
        getRunwayById(airportId, runwayId)
    }

    suspend fun deleteRunway(airportId: String, runwayId: String) {
        runwaysService.deleteRunway(airportId, runwayId)
        preferencesHelper.userId?.let {
            val runway = runwayDao.getRunwayById(it, airportId, runwayId)
            runway?.let { entity ->
                runwayDao.delete(entity)
            }
        }
    }
}