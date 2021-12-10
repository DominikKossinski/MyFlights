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
            BasePath.FLIGHTS -> {
                val flightId = extractEntityId(requestPath, basePath.path)
                val requestBody = gson.fromJson(body, FlightRequest::class.java)
                val flight = flights.find { it.flightId == flightId } ?: return notFoundResponse()
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
                Log.d("MyLogServer", "Departure runway: $departureRunway")
                flights.remove(flight)
                flights.add(
                    flight.copy(
                        note = requestBody.note,
                        distance = requestBody.distance,
                        airplane = airplane,
                        departureDate = requestBody.departureDate,
                        arrivalDate = requestBody.arrivalDate,
                        departureAirport = departureAirport,
                        departureRunway = departureRunway,
                        arrivalAirport = arrivalAirport,
                        arrivalRunway = arrivalRunway
                    )
                )
            }
        }
        return noContentResponse()
    }
}