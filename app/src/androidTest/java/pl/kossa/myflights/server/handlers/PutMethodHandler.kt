package pl.kossa.myflights.server.handlers

import okhttp3.mockwebserver.MockResponse
import pl.kossa.myflights.api.models.Airplane
import pl.kossa.myflights.api.models.Airport
import pl.kossa.myflights.api.models.Flight
import pl.kossa.myflights.api.models.Runway
import pl.kossa.myflights.api.requests.AirplaneRequest
import pl.kossa.myflights.api.requests.AirportRequest
import pl.kossa.myflights.api.requests.RunwayRequest
import pl.kossa.myflights.server.BasePath

class PutMethodHandler(
    airplanes: ArrayList<Airplane>,
    airports: ArrayList<Airport>,
    runways: ArrayList<Runway>,
    flights: ArrayList<Flight>
) : MethodHandler(airplanes, airports, runways, flights) {

    override fun handleRequest(requestPath: String, body: String): MockResponse {
        when (val basePath = findBasePath(requestPath)) {
            BasePath.AIRPORTS -> {
                val id = extractEntityId(requestPath, basePath.path)
                val airport = airports.find { it.airportId == id } ?: return notFoundResponse()
                val requestBody = gson.fromJson(body, AirportRequest::class.java)
                airports.remove(airport)
                airports.add(
                    airport.copy(
                        name = requestBody.name,
                        city = requestBody.city,
                        icaoCode = requestBody.icaoCode,
                        towerFrequency = requestBody.towerFrequency,
                        groundFrequency = requestBody.groundFrequency,
                        imageUrl = requestBody.imageUrl,
                    )
                )
            }
            BasePath.AIRPLANES -> {
                val id = extractEntityId(requestPath, basePath.path)
                val airplane = airplanes.find { it.airplaneId == id } ?: return notFoundResponse()
                val requestBody = gson.fromJson(body, AirplaneRequest::class.java)
                airplanes.remove(airplane)
                airplanes.add(
                    airplane.copy(
                        name = requestBody.name,
                        maxSpeed = requestBody.maxSpeed,
                        weight = requestBody.weight,
                        image = null // TODO image
                    )
                )
            }
            BasePath.RUNWAYS -> {
                val airportId = extractAirportId(requestPath)
                val airport =
                    airports.find { it.airportId == airportId } ?: return notFoundResponse()
                val runwayId = extractRunwayId(requestPath)
                val runway =
                    airport.runways.find { it.runwayId == runwayId } ?: return notFoundResponse()
                val responseBody = gson.fromJson(body, RunwayRequest::class.java)
                val newRunway = runway.copy(
                    name = responseBody.name,
                    length = responseBody.length,
                    heading = responseBody.heading,
                    ilsFrequency = responseBody.ilsFrequency
                )
                runways.remove(runway)
                runways.add(newRunway)
                airports.remove(airport)
                val newRunways = arrayListOf<Runway>().apply {
                    addAll(airport.runways.filter { it.runwayId != runwayId })
                    add(newRunway)
                }
                airports.add(airport.copy(runways = newRunways))
            }
            BasePath.FLIGHTS -> TODO()
        }
        return noContentResponse()
    }
}