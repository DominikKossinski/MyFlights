package pl.kossa.myflights.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import pl.kossa.myflights.api.models.Image

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
) {
    companion object {

        fun fromApiImage(image: Image?): ImageModel? {
            return image?.let { ImageModel(it.imageId, it.url, it.thumbnailUrl)}
        }
    }
}