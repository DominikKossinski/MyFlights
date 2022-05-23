package pl.kossa.myflights.api.models

import androidx.room.ColumnInfo
import org.jetbrains.annotations.NotNull

data class Image(
    val imageId: String,
    @NotNull @ColumnInfo(name = "image_url")
    val url: String,
    @NotNull @ColumnInfo(name = "image_thumbnail_url")
    val thumbnailUrl: String
)