package pl.kossa.myflights.api.responses

data class ApiErrorBody(
    val message: String,
    val description: String
)

data class ApiError(
    val code: Int,
    val body: ApiErrorBody?
)
