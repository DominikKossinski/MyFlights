package pl.kossa.myflights.server.handlers

import com.google.gson.GsonBuilder
import okhttp3.mockwebserver.MockResponse
import pl.kossa.myflights.api.models.Airplane
import pl.kossa.myflights.api.models.Airport
import pl.kossa.myflights.api.models.Flight
import pl.kossa.myflights.api.models.Runway
import pl.kossa.myflights.api.responses.ApiErrorBody
import pl.kossa.myflights.api.responses.CreatedResponse
import pl.kossa.myflights.server.BasePath
import java.lang.Exception

abstract class MethodHandler(
    protected val airplanes: ArrayList<Airplane>,
    protected val airports: ArrayList<Airport>,
    protected val runways: ArrayList<Runway>,
    protected val flights: ArrayList<Flight>
) {
    protected val gson = GsonBuilder().apply {
        setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    }.create()


    protected fun findBasePath(path: String): BasePath {
        val runwayRegex = "/api/airports/[0-9]+/runways[/0-9]*".toRegex()
        return when {
            path.matches(runwayRegex) -> BasePath.RUNWAYS
            path.contains("/api/airplanes") -> BasePath.AIRPLANES
            path.contains("/api/airports") -> BasePath.AIRPORTS
            path.contains("/api/flights") -> BasePath.FLIGHTS
            else -> throw Exception()
        }
    }

    protected fun isIdPath(path: String, basePath: String): Boolean {
        val regex = if (basePath == "/api/runways") {
            "/api/airports/[0-9]+/runways/[0-9]+".toRegex()
        } else "$basePath/[0-9]+".toRegex()
        return path.matches(regex)
    }

    protected fun extractAirportId(path: String): String {
        return path.replace("/runways[/0-9]*".toRegex(), "").replace("/api/airports/", "")
    }

    protected fun extractRunwayId(path: String): String {
        return path.replace("/api/airports/[0-9]+/runways/".toRegex(), "")
    }

    protected fun extractEntityId(path: String, basePath: String): String {
        return path.replace("$basePath/", "")
    }

    abstract fun handleRequest(requestPath: String, body: String): MockResponse

    protected fun notFoundResponse() =
        MockResponse().setResponseCode(404).setBody(gson.toJson(ApiErrorBody("", "")))

    protected fun noContentResponse() =
        MockResponse().setResponseCode(204)

    protected fun badRequestResponse() =
        MockResponse().setResponseCode(400).setBody(gson.toJson(ApiErrorBody("", "")))

    protected fun createdResponse(entityId: String) =
        MockResponse().setResponseCode(201).setBody(gson.toJson(CreatedResponse(entityId)))
}