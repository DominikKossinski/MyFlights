package pl.kossa.myflights.api.exceptions

import pl.kossa.myflights.api.responses.ApiError

class ApiServerException(val apiError: ApiError): Exception()