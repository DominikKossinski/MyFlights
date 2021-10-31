package pl.kossa.myflights.api.call

data class ApiResponse<out T: Any>(val body: T?)