package pl.kossa.myflights.room.entities

import androidx.room.*
import org.jetbrains.annotations.NotNull

@Entity
data class AirplaneModel(
    @PrimaryKey
    val airplaneId: String,

    @NotNull
    @ColumnInfo(name = "Name")
    val name: String,

    @ColumnInfo(name = "MaxSpeed")
    val maxSpeed: Int?,

    @ColumnInfo(name = "Weight")
    val weight: Int?,

    @ColumnInfo(name="imageId")
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
    val image: ImageModel
)