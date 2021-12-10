package pl.kossa.myflights.api.server.models


data class User(
    val userId: String,

    val nick: String,

    val email: String?,

    val avatar: Image?
)