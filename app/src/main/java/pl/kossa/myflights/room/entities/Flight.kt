package pl.kossa.myflights.room.entities

import androidx.room.*
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
)

@Entity
data class ShareDataModel(
    @PrimaryKey
    val sharedFlightId: String,
    @ColumnInfo(name = "flightId")
    val flightId: String,
    @ColumnInfo(name = "sharedUserId")
    val sharedUserId: String,
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
    val sharedUserData: SharedUserData
)

@Entity
data class SharedUserDataModel(
    @PrimaryKey
    val userId: String,
    @ColumnInfo(name = "sharedUserEmail")
    val email: String,
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
)

@Entity
data class OwnerDataModel(
    @PrimaryKey
    val ownerId: String,
    @ColumnInfo(name = "ownerEmail")
    val email: String,
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
)
