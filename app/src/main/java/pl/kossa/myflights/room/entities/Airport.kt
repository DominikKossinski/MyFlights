package pl.kossa.myflights.room.entities

import androidx.room.*
import pl.kossa.myflights.api.models.Airport

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
    val imageModel: ImageModel?
) {
}