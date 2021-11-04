package pl.kossa.myflights.server

enum class BasePath(val path: String) {
    AIRPORTS("/api/airports"),
    AIRPLANES("/api/airplanes"),
    RUNWAYS("/api/runways"),
    FLIGHTS("/api/flights")
}