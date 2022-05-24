package pl.kossa.myflights.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity
data class ImageModel(
    @PrimaryKey
    val imageId: String,
    @NotNull
    @ColumnInfo(name = "Url")
    val url: String,
    @NotNull
    @ColumnInfo(name = "ThumbnailUrl")
    val thumbnailUrl: String
)