package pl.kossa.myflights.api.server.models

data class Airplane(
    val airplaneId: String,

    val name: String,

    val maxSpeed: Int?,

    val weight: Int?,

    val image: Image?,

    val userId: String
)