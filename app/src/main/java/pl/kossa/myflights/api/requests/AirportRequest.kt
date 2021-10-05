package pl.kossa.myflights.api.requests

data class AirportRequest(
    val name: String,
    val city: String,
    val icaoCode: String,
    val towerFrequency: String?,
    val groundFrequency: String?,
    val imageUrl: String?
)