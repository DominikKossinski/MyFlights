package pl.kossa.myflights.api.server.models

data class Runway(

    val runwayId: String,

    val name: String,

    val length: Int,

    val heading: Int,

    val ilsFrequency: String?,

    val image: Image?,

    val userId: String
)