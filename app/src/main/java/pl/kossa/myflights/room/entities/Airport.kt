package pl.kossa.myflights.room.entities

import androidx.room.*

@Entity
data class AirportModel(

    @PrimaryKey
    val airportId: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "city")
    val city: String,

    @ColumnInfo(name = "icaoCode")
    val icaoCode: String,

    @ColumnInfo(name = "towerFrequency")
    val towerFrequency: String?,

    @ColumnInfo(name = "groundFrequency")
    val groundFrequency: String?,

    @ColumnInfo(name = "imageId")
    val imageId: String?,

    @ColumnInfo(name = "userId")
    val userId: String
)

data class Airport(
    @Embedded
    val airport: AirportModel,
    @Relation(
        parentColumn = "imageId",
        entityColumn = "imageId"
    )
    val image: ImageModel?,
    @Relation(
        entity = RunwayModel::class,
        parentColumn = "airportId",
        entityColumn = "airportId"
    )
    val runways: List<Runway>
) {

    companion object {
        fun fromApiAirport(airport: pl.kossa.myflights.api.models.Airport): Airport {
            return Airport(
                AirportModel(
                    airport.airportId,
                    airport.name,
                    airport.city,
                    airport.icaoCode,
                    airport.towerFrequency,
                    airport.groundFrequency,
                    airport.image?.imageId,
                    airport.userId
                ),
                ImageModel.fromApiImage(airport.image),
                airport.runways.map {
                    Runway.fromApiRunway(airport.airportId, it)
                }
            )
        }
    }
}