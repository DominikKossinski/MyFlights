package pl.kossa.myflights.server.handlers

import android.util.Log
import okhttp3.mockwebserver.MockResponse
import pl.kossa.myflights.api.server.models.Airplane
import pl.kossa.myflights.api.server.models.Airport
import pl.kossa.myflights.api.server.models.Flight
import pl.kossa.myflights.api.server.models.Runway
import pl.kossa.myflights.api.server.requests.AirplaneRequest
import pl.kossa.myflights.api.server.requests.AirportRequest
import pl.kossa.myflights.api.server.requests.FlightRequest
import pl.kossa.myflights.api.server.requests.RunwayRequest
import pl.kossa.myflights.server.BasePath

class PostMethodHandler(
    airplanes: ArrayList<Airplane>,
    airports: ArrayList<Airport>,
    runways: ArrayList<Runway>,
    flights: ArrayList<Flight>
) : MethodHandler(airplanes, airports, runways, flights) {

    override fun handleRequest(requestPath: String, body: String): MockResponse {
        val basePath = findBasePath(requestPath)
        Log.d("MyLogServer", "POST")
        Log.d("MyLogServer", "Path = $requestPath")
        val id = when (basePath) {
            BasePath.AIRPLANES -> {
                val requestBody = gson.fromJson(body, AirplaneRequest::class.java)
                val lastAirplane = airplanes.last()
                val airplane = Airplane(
                    (lastAirplane.airplaneId.toInt() + 1).toString(),
                    requestBody.name,
                    requestBody.maxSpeed,
                    requestBody.maxSpeed,
                    null, // TODO image
                    "1"
                )
                airplanes.add(airplane)
                airplane.airplaneId
            }
            BasePath.AIRPORTS -> {
                val requestBody = gson.fromJson(body, AirportRequest::class.java)
                val lastAirport = airports.last()
                val airport = Airport(
                    (lastAirport.airportId.toInt() + 1).toString(),
                    requestBody.name,
                    requestBody.city,
                    requestBody.icaoCode,
                    requestBody.towerFrequency,
                    requestBody.groundFrequency,
                    requestBody.imageUrl,
                    listOf(),
                    "1"
                )
                airports.add(airport)
                airport.airportId
            }
            BasePath.FLIGHTS -> {
                val requestBody = gson.fromJson(body, FlightRequest::class.java)
                val lastFlight = flights.last()
                val newId = (lastFlight.flightId.toInt() + 1).toString()
                val airplane = airplanes.find { it.airplaneId == requestBody.airplaneId }
                    ?: return notFoundResponse()
                val departureAirport =
                    airports.find { it.airportId == requestBody.departureAirportId }
                        ?: return notFoundResponse()
                val departureRunway =
                    departureAirport.runways.find { it.runwayId == requestBody.departureRunwayId }
                        ?: return notFoundResponse()

                val arrivalAirport = airports.find { it.airportId == requestBody.arrivalAirportId }
                    ?: return notFoundResponse()
                val arrivalRunway =
                    arrivalAirport.runways.find { it.runwayId == requestBody.arrivalRunwayId }
                        ?: return notFoundResponse()
                val flight = Flight(
                    newId,
                    requestBody.note,
                    requestBody.distance,
                    null, //TODO image,
                    requestBody.departureDate,
                    requestBody.arrivalDate,
                    "1",
                    airplane,
                    departureAirport,
                    departureRunway,
                    arrivalAirport,
                    arrivalRunway
                )
                flights.add(flight)
                newId
            }
            BasePath.RUNWAYS -> {
                val airportId = extractAirportId(requestPath)
                val airport =
                    airports.find { it.airportId == airportId } ?: return notFoundResponse()
                val newId = (runways.last().runwayId.toInt() + 1).toString()
                val requestBody = gson.fromJson(body, RunwayRequest::class.java)
                val runway = Runway(
                    newId,
                    requestBody.name,
                    requestBody.length,
                    requestBody.heading,
                    requestBody.ilsFrequency,
                    null,
                    "1"
                )
                airports.remove(airport)
                val newRunways = arrayListOf<Runway>().apply {
                    addAll(airport.runways)
                    add(runway)
                }
                airports.add(airport.copy(runways = newRunways))
                Log.d("MyLog", "Airports $airports")
                runways.add(runway)
                newId
            }
        }
        return createdResponse(id)
    }

}