package pl.kossa.myflights.room.entities

import androidx.room.*
import pl.kossa.myflights.api.responses.StatisticsResponse

@Entity
data class StatisticsModel(
    @PrimaryKey
    val userId: String,

    @ColumnInfo(name = "flightHours")
    val flightHours: Double,

    @ColumnInfo(name = "favouriteAirplaneId")
    val favouriteAirplaneId: String?,

    @ColumnInfo(name = "favouriteDepartureAirportId")
    val favouriteDepartureAirportId: String?,

    @ColumnInfo(name = "favouriteArrivalAirportId")
    val favouriteArrivalAirportId: String?
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
        entityColumn = "airportId",
        entity = AirportModel::class
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

data class TopNAirplane(
    @Embedded
    val topNAirplane: TopNAirplaneModel,
    @Relation(
        parentColumn = "airplaneId",
        entityColumn = "airplaneId",
        entity = AirplaneModel::class
    )
    val airplane: Airplane
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
        parentColumn = "userId",
        entityColumn = "userId",
        entity = TopNAirplaneModel::class
    )
    val top5Airplanes: List<TopNAirplane>,
    @Relation(
        parentColumn = "favouriteDepartureAirportId",
        entityColumn = "airportId",
        entity = AirportModel::class
    )
    val favouriteDepartureAirport: Airport?,
    @Ignore
    val top5DepartureAirports: List<TopNAirport>,
    @Relation(
        parentColumn = "favouriteArrivalAirportId",
        entityColumn = "airportId",
        entity = AirportModel::class
    )
    val favouriteArrivalAirport: Airport?,
    @Ignore
    val top5ArrivalAirports: List<TopNAirport>,
) {
    constructor(
        statistics: StatisticsModel,
        favouriteAirplane: Airplane?,
        top5Airplanes: List<TopNAirplane>,
        favouriteDepartureAirport: Airport?,
        favouriteArrivalAirport: Airport?,
    ) : this(statistics, favouriteAirplane, top5Airplanes, favouriteDepartureAirport, emptyList(), favouriteArrivalAirport, emptyList())

    companion object {
        fun fromApiStatisticsResponse(
            userId: String,
            statisticsResponse: StatisticsResponse
        ): Statistics {
            return Statistics(
                StatisticsModel(
                    userId,
                    statisticsResponse.flightHours,
                    statisticsResponse.favouriteArrivalAirport?.airportId,
                    statisticsResponse.favouriteDepartureAirport?.airportId,
                    statisticsResponse.favouriteArrivalAirport?.airportId
                ),
                statisticsResponse.favouriteAirplane?.let {
                    Airplane.fromApiAirplane(it)
                },
                statisticsResponse.top5Airplanes.mapIndexed { i, airplane ->
                    TopNAirplane(
                        TopNAirplaneModel(
                            userId,
                            i,
                            airplane.item.airplaneId,
                            airplane.occurrences,
                        ),
                        Airplane.fromApiAirplane(airplane.item)
                    )
                },
                statisticsResponse.favouriteDepartureAirport?.let {
                    Airport.fromApiAirport(it)
                },
                statisticsResponse.top5DepartureAirports.mapIndexed { i, airport ->
                    TopNAirport(
                        TopNAirportModel(
                            userId,
                            i,
                            airport.item.airportId,
                            airport.occurrences,
                            true
                        ),
                        Airport.fromApiAirport(airport.item)
                    )
                },
                statisticsResponse.favouriteArrivalAirport?.let {
                    Airport.fromApiAirport(it)
                },
                statisticsResponse.top5ArrivalAirports.mapIndexed { i, airport ->
                    TopNAirport(
                        TopNAirportModel(
                            userId,
                            i,
                            airport.item.airportId,
                            airport.occurrences,
                            false
                        ),
                        Airport.fromApiAirport(airport.item)
                    )
                }
            )
        }
    }
}
