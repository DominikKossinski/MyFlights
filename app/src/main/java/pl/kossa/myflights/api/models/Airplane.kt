package pl.kossa.myflights.api.models

import androidx.room.*
import org.jetbrains.annotations.NotNull

@Entity
data class Airplane(
    @PrimaryKey
    val airplaneId: String,

    @NotNull @ColumnInfo(name = "airplane_name")
    val name: String,

    @ColumnInfo(name = "airplane_max_speed")
    val maxSpeed: Int?,

    @ColumnInfo(name = "airplane_weight")
    val weight: Int?,

    @Embedded
    val image: Image?,

    @NotNull @ColumnInfo(name ="airplane_user_id")
    val userId: String
)