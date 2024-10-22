package pl.kossa.myflights.compose.components.airport

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.kossa.myflights.R
import pl.kossa.myflights.room.entities.Airport
import pl.kossa.myflights.room.entities.AirportModel

@Composable
fun AirportHorizontalItem(
    airport: Airport,
    modifier: Modifier = Modifier,
    onAirportClick: (Airport) -> Unit = {}
) {

    Card(
        onClick = {
            onAirportClick.invoke(airport)
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary, shape = CircleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_airport_bg),
                    contentDescription = null,
                    modifier = Modifier
                        .size(42.dp)
                        .padding(8.dp)
                )
            }
            Column {
                Text(
                    text = airport.airport.name
                )
                Text(
                    text = airport.airport.icaoCode
                )
            }
        }
    }
}

@Composable
@Preview
private fun AirportHorizontalItemPreview() {
    AirportHorizontalItem(
        Airport(
            airport = AirportModel(
                airportId = "",
                name = "Okęcie",
                city = "Warszawa",
                icaoCode = "EPWA",
                towerFrequency = null,
                groundFrequency = null,
                imageId = null,
                userId = ""
            ),
            image = null,
            runways = emptyList()
        )
    )
}