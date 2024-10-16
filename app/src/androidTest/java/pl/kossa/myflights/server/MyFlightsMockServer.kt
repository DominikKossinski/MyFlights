package pl.kossa.myflights.server

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import pl.kossa.myflights.api.models.Airplane
import pl.kossa.myflights.api.models.Airport
import pl.kossa.myflights.api.models.Flight
import pl.kossa.myflights.api.models.Runway
import pl.kossa.myflights.server.handlers.DeleteMethodHandler
import pl.kossa.myflights.server.handlers.GetMethodHandler
import pl.kossa.myflights.server.handlers.PostMethodHandler
import pl.kossa.myflights.server.handlers.PutMethodHandler
import java.util.*

class MyFlightsMockServer {

    private val server = MockWebServer()

    private val airplanes = arrayListOf(
        Airplane("1", "A380", 50, 50, null, "1")
    )

    private val runways = arrayListOf(
        Runway("1", "11", 1250, 110, "114.5", null, "1"),
        Runway("2", "15", 1250, 150, "114.5", null, "1")
    )

    private val airports = arrayListOf(
        Airport(
            "1", "Okęcie", "Warszawa", "EPWA", "109.9",
            "123.5", null,
            arrayListOf(
                runways[0],
                runways[1]
            ),
            "1"
        )
    )

    private val flights = arrayListOf(
        Flight(
            "1",
            "Note from flight",
            1235,
            null,
            Date().apply { time -= 20 * 60 * 1_000 },
            Date(),
            "1",
            airplanes[0],
            airports[0],
            runways[0],
            airports[0],
            runways[1]
        )
    )

    private val getMethodHandler = GetMethodHandler(airplanes, airports, runways, flights)
    private val postMethodHandler = PostMethodHandler(airplanes, airports, runways, flights)
    private val putMethodHandler = PutMethodHandler(airplanes, airports, runways, flights)
    private val deleteMethodHandler = DeleteMethodHandler(airplanes, airports, runways, flights)

    init {
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                val requestPath = request.path ?: return MockResponse().setResponseCode(500)
                val method = request.method ?: return MockResponse().setResponseCode(500)
                val body = request.body.readUtf8()
                return when (method) {
                    "GET" -> getMethodHandler.handleRequest(requestPath, body)
                    "POST" -> postMethodHandler.handleRequest(requestPath, body)
                    "PUT" -> putMethodHandler.handleRequest(requestPath, body)
                    "DELETE" -> deleteMethodHandler.handleRequest(requestPath, body)
                    else -> MockResponse().setResponseCode(500)
                }
            }
        }
    }

    fun start(port: Int) {
        server.start(port)
    }

    fun shutdown() {
        server.shutdown()
    }

}