package pl.kossa.myflights.api.models


data class User(
    val userId: String,

    val nick: String,

    val email: String?,

    val avatar: Image?,

    val regulationsAccepted: Boolean,

    val providerType: ProviderType
)

enum class ProviderType{
    PASSWORD,
    GOOGLE
}