package pl.kossa.myflights.api.exceptions

import pl.kossa.myflights.api.responses.ApiError
import java.lang.Exception

class ApiServerException(val apiError: ApiError): Exception()