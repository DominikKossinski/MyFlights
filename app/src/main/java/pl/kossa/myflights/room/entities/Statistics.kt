package pl.kossa.myflights.room.entities

import androidx.room.*

@Entity
data class StatisticsModel(
    @PrimaryKey
    val userId: String,

    @ColumnInfo(name = "flightHours")
    val flightHours: Double,

    @ColumnInfo(name = "favouriteAirplaneId")
    val favouriteAirplaneId: String,

    @ColumnInfo(name = "favouriteDepartureAirportId")
    val favouriteDepartureAirportId: String,

    @ColumnInfo(name = "favouriteArrivalAirportId")
    val favouriteArrivalAirportId: String
)

@Entity(primaryKeys = ["userId", "order", "isDeparture"])
data class TopNAirportModel(
    @ColumnInfo(name = "userId")
    val userId: String,

    @ColumnInfo(name = "order")
    val order: Int,

    @ColumnInfo(name = "airportId")
    val airportId: String,

    @ColumnInfo(name = "occurrences")
    val occurrences: Int,

    @ColumnInfo(name = "isDeparture")
    val isDeparture: Boolean
)

data class TopNAirport(
    @Embedded
    val topNAirport: TopNAirportModel,
    @Relation(
        parentColumn = "airportId",
        entityColumn = "airportId"
    )
    val airport: Airport
)


@Entity(primaryKeys = ["userId", "order"])
data class TopNAirplaneModel(
    @ColumnInfo(name = "userId")
    val userId: String,

    @ColumnInfo(name = "order")
    val order: Int,

    @ColumnInfo(name = "airplaneId")
    val airplaneId: String,

    @ColumnInfo(name = "occurrences")
    val occurrences: Int
)

data class Statistics(
    @Embedded
    val statistics: StatisticsModel,
    @Relation(
        parentColumn = "favouriteAirplaneId",
        entityColumn = "airplaneId",
        entity = AirplaneModel::class
    )
    val favouriteAirplane: Airplane?,
    @Relation(
        parentColumn = "favouriteDepartureAirportId",
        entityColumn = "airportId",
        entity = AirportModel::class
    )
    val favouriteDepartureAirport: Airport?,
    @Relation(
        parentColumn = "favouriteArrivalAirportId",
        entityColumn = "airportId",
        entity = AirportModel::class
    )
    val favouriteArrivalAirport: Airport?
)
