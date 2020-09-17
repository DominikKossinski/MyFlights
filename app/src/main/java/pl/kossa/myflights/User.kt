package pl.kossa.myflights

data class User(
    val uid: String,
    val email: String,
    val isEmailVerified: Boolean
)