package pl.kossa.myflights.api.requests

import android.media.Image

data class RunwayRequest(
    val name: String,
    val length: Int,
    val heading: Int,
    val ilsFrequency: String?,
    val image: Image?
)
