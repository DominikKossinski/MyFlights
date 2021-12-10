package pl.kossa.myflights.server.handlers

import android.util.Log
import okhttp3.mockwebserver.MockResponse
import pl.kossa.myflights.api.server.models.Airplane
import pl.kossa.myflights.api.server.models.Airport
import pl.kossa.myflights.api.server.models.Flight
import pl.kossa.myflights.api.server.models.Runway
import pl.kossa.myflights.server.BasePath

class DeleteMethodHandler(
    airplanes: ArrayList<Airplane>,
    airports: ArrayList<Airport>,
    runways: ArrayList<Runway>,
    flights: ArrayList<Flight>
) : MethodHandler(airplanes, airports, runways, flights) {

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
            BasePath.RUNWAYS -> {
                val airportId = extractAirportId(requestPath)
                val runwayId = extractRunwayId(requestPath)
                val f =
                    flights.filter { it.departureRunway.runwayId == runwayId || it.arrivalRunway.runwayId == runwayId }
                if(f.isNotEmpty()) return badRequestResponse()
                val airport = airports.find { it.airportId == airportId } ?: return notFoundResponse()
                val runway = airport.runways.find { it.runwayId == runwayId } ?: return notFoundResponse()
                runways.remove(runway)
                airports.remove(airport)
                airports.add(airport.copy(runways = airport.runways.filter { it.runwayId != runwayId }))
            }
        }
        return noContentResponse()
    }
}