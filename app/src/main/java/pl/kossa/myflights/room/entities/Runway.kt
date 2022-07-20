package pl.kossa.myflights.room.entities

import androidx.room.*

@Entity
data class RunwayModel(
    @PrimaryKey
    val runwayId: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "length")
    val length: Int,

    @ColumnInfo(name = "heading")
    val heading: Int,

    @ColumnInfo(name = "ilsFrequency")
    val ilsFrequency: String?,

    @ColumnInfo(name = "imageId")
    val imageId: String?,

    @ColumnInfo(name = "airportId")
    val airportId: String,

    @ColumnInfo(name = "userId")
    val userId: String
)

data class Runway(
    @Embedded
    val runway: RunwayModel,
    @Relation(
        entity = ImageModel::class,
        parentColumn = "imageId",
        entityColumn = "imageId"
    )
    val image: ImageModel?
) {

    companion object {
        fun fromApiRunway(
            airportId: String,
            runway: pl.kossa.myflights.api.models.Runway
        ): Runway {
            return Runway(
                RunwayModel(
                    runway.runwayId,
                    runway.name,
                    runway.length,
                    runway.heading,
                    runway.ilsFrequency,
                    runway.image?.imageId,
                    airportId,
                    runway.userId
                ),
                ImageModel.fromApiImage(runway.image)
            )
        }
    }
}
