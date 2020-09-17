package pl.kossa.myflights.api.models

data class Runway(

    val runwayId: Int,

    val name: String,

    val length: Int,

    val heading: Int,

    val ilsFrequency: String?,

    val imageUrl: String?,

    val airport: Airport
)