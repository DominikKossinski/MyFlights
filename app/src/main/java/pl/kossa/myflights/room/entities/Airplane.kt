package pl.kossa.myflights.room.entities

import androidx.room.*
import org.jetbrains.annotations.NotNull

@Entity
data class AirplaneModel(
    @PrimaryKey
    val airplaneId: String,

    @NotNull
    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "maxSpeed")
    val maxSpeed: Int?,

    @ColumnInfo(name = "weight")
    val weight: Int?,

    @ColumnInfo(name = "imageId")
    val imageId: String?,

    @NotNull
    @ColumnInfo(name = "userId")
    val userId: String
)

data class Airplane(
    @Embedded
    val airplane: AirplaneModel,
    @Relation(
        parentColumn = "imageId",
        entityColumn = "imageId"
    )
    val image: Image?,
    @Relation(
        parentColumn = "airportId",
        entityColumn = "airportId"
    )
    val runways: List<Runway>
)