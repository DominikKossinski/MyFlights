package pl.kossa.myflights.api.models

data class User(
    val userId: String,

    val nick: String?,

    val email: String?,

    val imageUrl: String?
)