package pl.kossa.myflights.room.entities

import androidx.room.*
import pl.kossa.myflights.api.responses.flights.FlightResponse
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
    val arrivalRunwayId: String,

    @ColumnInfo(name = "ownerId")
    val ownerId: String
)

data class Flight(
    @Embedded
    val flight: FlightModel,
    @Relation(
        parentColumn = "airplaneId",
        entityColumn = "airplaneId",
        entity = AirplaneModel::class
    )
    val airplane: Airplane,
    @Relation(
        parentColumn = "departureAirportId",
        entityColumn = "airportId",
        entity = AirportModel::class
    )
    val departureAirport: Airport,
    @Relation(
        parentColumn = "departureRunwayId",
        entityColumn = "runwayId",
        entity = RunwayModel::class
    )
    val departureRunway: Runway,
    @Relation(
        parentColumn = "arrivalAirportId",
        entityColumn = "airportId",
        entity = AirportModel::class
    )
    val arrivalAirport: Airport,
    @Relation(
        parentColumn = "arrivalRunwayId",
        entityColumn = "runwayId",
        entity = RunwayModel::class
    )
    val arrivalRunway: Runway,
    @Relation(
        parentColumn = "ownerId",
        entityColumn = "ownerId",
        entity = OwnerDataModel::class
    )
    val ownerData: OwnerData,
    @Relation(
        parentColumn = "flightId",
        entityColumn = "flightId",
        entity = ShareDataModel::class
    )
    val sharedUsers: List<ShareData>
) {

    companion object {
        fun fromApiFlight(flightResponse: FlightResponse): Flight {
            val flight = flightResponse.flight
            return Flight(
                FlightModel(
                    flight.flightId,
                    flight.note,
                    flight.distance,
                    flight.image?.imageId,
                    flight.departureDate,
                    flight.arrivalDate,
                    flight.userId,
                    flight.airplane.airplaneId,
                    flight.departureAirport.airportId,
                    flight.departureRunway.runwayId,
                    flight.arrivalAirport.airportId,
                    flight.arrivalRunway.runwayId,
                    flight.userId
                ),
                Airplane.fromApiAirplane(flight.airplane),
                Airport.fromApiAirport(flight.departureAirport),
                Runway.fromApiRunway(flight.departureAirport.airportId, flight.departureRunway),
                Airport.fromApiAirport(flight.arrivalAirport),
                Runway.fromApiRunway(flight.arrivalAirport.airportId, flight.arrivalRunway),
                OwnerData.fromApiOwnerData(flightResponse.ownerData),
                flightResponse.sharedUsers.map {
                    ShareData.fromApiShareData(flight.flightId, it)
                }
            )
        }
    }
}

@Entity
data class ShareDataModel(
    @PrimaryKey
    val sharedFlightId: String,
    @ColumnInfo(name = "flightId")
    val flightId: String,
    @ColumnInfo(name = "sharedUserId")
    val sharedUserId: String?,
    @ColumnInfo(name = "isConfirmed")
    val isConfirmed: Boolean
)

data class ShareData(
    @Embedded
    val sharedData: ShareDataModel,
    @Relation(
        parentColumn = "sharedUserId",
        entityColumn = "userId",
        entity = SharedUserDataModel::class
    )
    val sharedUserData: SharedUserData?
) {

    companion object {
        fun fromApiShareData(
            flightId: String,
            shareData: pl.kossa.myflights.api.responses.flights.ShareData
        ): ShareData {
            return ShareData(
                ShareDataModel(
                    shareData.sharedFlightId,
                    flightId,
                    shareData.userData?.userId, // TODO refactor to nullable
                    shareData.isConfirmed
                ),
                SharedUserData.fromApiSharedUserData(shareData.userData)
            )
        }
    }
}

@Entity
data class SharedUserDataModel(
    @PrimaryKey
    val userId: String,
    @ColumnInfo(name = "sharedUserEmail")
    val email: String?,
    @ColumnInfo(name = "sharedUserNick")
    val nick: String,
    @ColumnInfo(name = "sharedUserImageId")
    val imageId: String?
)

data class SharedUserData(
    @Embedded
    val sharedUser: SharedUserDataModel,
    @Relation(
        parentColumn = "sharedUserImageId",
        entityColumn = "imageId",
        entity = ImageModel::class
    )
    val image: ImageModel?
) {
    companion object {
        fun fromApiSharedUserData(sharedUserData: pl.kossa.myflights.api.responses.sharedflights.SharedUserData?): SharedUserData? {
            return sharedUserData?.let {
                SharedUserData(
                    SharedUserDataModel(
                        it.userId,
                        it.email,
                        it.nick,
                        it.avatar?.imageId
                    ),
                    ImageModel.fromApiImage(it.avatar)
                )
            }
        }
    }
}

@Entity
data class OwnerDataModel(
    @PrimaryKey
    val ownerId: String,
    @ColumnInfo(name = "ownerEmail")
    val email: String?,
    @ColumnInfo(name = "ownerNick")
    val nick: String,
    @ColumnInfo(name = "ownerImageId")
    val imageId: String?
)

data class OwnerData(
    @Embedded
    val ownerData: OwnerDataModel,
    @Relation(
        parentColumn = "ownerImageId",
        entityColumn = "imageId",
        entity = ImageModel::class
    )
    val image: ImageModel?
) {

    companion object {
        fun fromApiOwnerData(sharedUserData: pl.kossa.myflights.api.responses.sharedflights.SharedUserData): OwnerData {
            return OwnerData(
                OwnerDataModel(
                    sharedUserData.userId,
                    sharedUserData.email,
                    sharedUserData.nick,
                    sharedUserData.avatar?.imageId
                ),
                ImageModel.fromApiImage(sharedUserData.avatar)
            )
        }
    }
}
