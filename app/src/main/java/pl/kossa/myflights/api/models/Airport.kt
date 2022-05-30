package pl.kossa.myflights.api.models

data class Airport(
    val airportId: String,

    val name: String,

    val city: String,

    val icaoCode: String,

    val towerFrequency: String?,

    val groundFrequency: String?,

    val image: Image?,

    val runways: List<Runway>,

    val userId: String
)