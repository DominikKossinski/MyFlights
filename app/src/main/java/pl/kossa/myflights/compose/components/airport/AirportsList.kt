package pl.kossa.myflights.compose.components.airport

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pl.kossa.myflights.navigation.AirportRoute
import pl.kossa.myflights.room.entities.Airport


@Composable
fun AirportsList(
    navController: NavController,
    airports: List<Airport>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = contentPadding
    ) {
        items(if(airports.isNotEmpty()) 50 else 0) { index: Int -> // TODO remove additional items

            val airport = airports.get(index % airports.size)
            AirportHorizontalItem(
                airport = airport,
                onAirportClick = { item: Airport ->
                    navController.navigate(
                        AirportRoute(
                            item.airport.airportId,
                            item.airport.icaoCode
                        )
                    )
                }
            )
        }
    }
}