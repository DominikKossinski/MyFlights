package pl.kossa.myflights.api.server.requests

data class UserRequest(
    val nick: String,
    val imageId: String?
)