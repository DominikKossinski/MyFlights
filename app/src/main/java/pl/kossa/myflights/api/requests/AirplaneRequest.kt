package pl.kossa.myflights.api.requests

data class AirplaneRequest(

    val name: String,

    val maxSpeed: Int?,

    val weight: Int?,

    val imageUrl: String?
)