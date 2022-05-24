package pl.kossa.myflights.room.entities

import androidx.room.*
import pl.kossa.myflights.api.models.Airport
import pl.kossa.myflights.api.models.Runway
import java.util.*

@Entity
data class FlightModel(
    @PrimaryKey
    val flightId: String,

    @ColumnInfo(name = "note")
    val note: String?,

    @ColumnInfo(name = "distance")
    val distance: Int?,

    @ColumnInfo(name = "imageId")
    val imageId: String?,

    @ColumnInfo(name = "departureDate")
    val departureDate: Date,

    @ColumnInfo(name = "arrivalDate")
    val arrivalDate: Date,

    @ColumnInfo(name = "userId")
    val userId: String,

    @ColumnInfo(name = "airplaneId")
    val airplaneId: String,

    @ColumnInfo(name = "departureAirportId")
    val departureAirportId: String,

    @ColumnInfo(name = "departureRunwayId")
    val departureRunwayId: String,

    @ColumnInfo(name = "arrivalAirportId")
    val arrivalAirportId: String,

    @ColumnInfo(name = "arrivalRunwayId")
    val arrivalRunwayId: String
)

data class Flight(
    @Embedded
    val flight: FlightModel,
    @Relation(
        parentColumn = "airplaneId",
        entityColumn = "airplaneId"
    )
    val airplane: Airplane,
    @Relation(
        parentColumn = "departureAirportId",
        entityColumn = "airportId"
    )
    val departureAirport: Airport,
    @Relation(
        parentColumn = "departureRunwayId",
        entityColumn = "runwayId"
    )
    val departureRunway: Runway,
    @Relation(
        parentColumn = "arrivalAirportId",
        entityColumn = "airportId"
    )
    val arrivalAirport: Airport,
    @Relation(
        parentColumn = "arrivalRunwayId",
        entityColumn = "runwayId"
    )
    val arrivalRunway: Runway
)
