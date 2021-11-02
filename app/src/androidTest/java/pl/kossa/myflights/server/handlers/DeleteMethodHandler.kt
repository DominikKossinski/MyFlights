package pl.kossa.myflights.server.handlers

import android.util.Log
import okhttp3.mockwebserver.MockResponse
import pl.kossa.myflights.api.models.Airplane
import pl.kossa.myflights.api.models.Airport
import pl.kossa.myflights.api.models.Flight
import pl.kossa.myflights.server.BasePath

class DeleteMethodHandler(
    airplanes: ArrayList<Airplane>,
    airports: ArrayList<Airport>,
    flights: ArrayList<Flight>
) : MethodHandler(airplanes, airports, flights) {

    override fun handleRequest(requestPath: String, body: String): MockResponse {
        val basePath = findBasePath(requestPath)
        Log.d("MyLogServer", "Path = $requestPath")
        Log.d("MyLogServer", "IsIdPath = ${isIdPath(requestPath, basePath.path)}")
        Log.d("MyLogServer", "BasePath = ${findBasePath(requestPath)}")
        when (basePath) {
            BasePath.AIRPORTS -> {
                val id = extractEntityId(requestPath, basePath.path)
                val f =
                    flights.filter { it.arrivalAirport.airportId == id || it.departureAirport.airportId == id }
                if (f.isNotEmpty()) return badRequestResponse()

                val airport = airports.find { it.airportId == id }
                    ?: return notFoundResponse()
                airports.remove(airport)
            }
            BasePath.AIRPLANES -> {
                val id = extractEntityId(requestPath, basePath.path)
                val f = flights.filter { it.airplane.airplaneId == id }
                if (f.isNotEmpty()) return badRequestResponse()

                val airplane = airplanes.find { it.airplaneId == id }
                    ?: return notFoundResponse()
                airplanes.remove(airplane)
            }
            BasePath.FLIGHTS -> {
                val id = extractEntityId(requestPath, basePath.path)
                val flight = flights.find { it.flightId == id }
                    ?: return notFoundResponse()
                flights.remove(flight)
            }
            BasePath.RUNWAYS -> TODO()
        }
        return noContentResponse()
    }
}