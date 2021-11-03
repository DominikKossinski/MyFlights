package pl.kossa.myflights.server.handlers

import android.util.Log
import okhttp3.mockwebserver.MockResponse
import pl.kossa.myflights.api.models.Airplane
import pl.kossa.myflights.api.models.Airport
import pl.kossa.myflights.api.models.Flight
import pl.kossa.myflights.api.models.Runway
import pl.kossa.myflights.server.BasePath

class GetMethodHandler(
    airplanes: ArrayList<Airplane>,
    airports: ArrayList<Airport>,
    runways: ArrayList<Runway>,
    flights: ArrayList<Flight>
) : MethodHandler(airplanes, airports, runways, flights) {

    override fun handleRequest(requestPath: String, body: String): MockResponse {
        val index = if (requestPath.contains("?"))
            requestPath.indexOf("?")
        else null
        val path = index?.let { requestPath.substring(0, it) } ?: requestPath
        val basePath = findBasePath(path)
        Log.d("MyLogServer", "GET")
        Log.d("MyLogServer", "Path = $path")
        Log.d("MyLogServer", "IsIdPath = ${isIdPath(path, basePath.path)}")
        Log.d("MyLogServer", "BasePath = ${findBasePath(path)}")
        if (isIdPath(path, basePath.path)) {
            if (basePath == BasePath.RUNWAYS) {
                val airportId = extractAirportId(requestPath)
                Log.d("MyLogServer", "AirportId = $airportId")
                val airport = airports.find { it.airportId == airportId } ?: return notFoundResponse()
                val runwayId = extractRunwayId(requestPath)
                Log.d("MyLogServer", "RunwayId = $runwayId")
                val runway = airport.runways.find { it.runwayId == runwayId } ?: return notFoundResponse()
                return MockResponse()
                    .setResponseCode(200)
                    .setBody(gson.toJson(runway))
            } else {
                val id = extractEntityId(path, basePath.path)
                return when (basePath) {
                    BasePath.AIRPORTS -> {
                        val airport = airports.find { it.airportId == id }
                        Log.d("MyLogServer", "Returning Airport = $airport")
                        airport?.let {
                            MockResponse()
                                .setResponseCode(200)
                                .setBody(gson.toJson(it))
                        } ?: MockResponse().setResponseCode(404)
                    }
                    BasePath.AIRPLANES -> {
                        val airplane = airplanes.find { it.airplaneId == id }
                        airplane?.let {
                            MockResponse()
                                .setResponseCode(200)
                                .setBody(gson.toJson(it))
                        } ?: MockResponse().setResponseCode(404)
                    }
                    else -> MockResponse().setResponseCode(500)
                }
            }
        } else {
            Log.d("MyLogServer", "List Request")
            return when (basePath) {
                BasePath.AIRPORTS -> MockResponse()
                    .setResponseCode(200)
                    .setBody(gson.toJson(airports))
                BasePath.AIRPLANES -> MockResponse()
                    .setResponseCode(200)
                    .setBody(gson.toJson(airplanes))
                BasePath.FLIGHTS -> MockResponse()
                    .setResponseCode(200)
                    .setBody(gson.toJson(flights))
                BasePath.RUNWAYS -> {
                    val airportId = extractAirportId(requestPath)
                    Log.d("MyLogServer", "Getting runways airportId = $airportId")
                    val airport = airports.find { it.airportId == airportId } ?: return notFoundResponse()
                    MockResponse()
                        .setResponseCode(200)
                        .setBody(gson.toJson(airport.runways))
                }
            }
        }
    }
}