package pl.kossa.myflights.api.models

data class Airplane(
    val airplaneId: String,

    val name: String,

    val maxSpeed: Int?,

    val weight: Int?,

    val imageUrl: String?,

    val userId: String
)