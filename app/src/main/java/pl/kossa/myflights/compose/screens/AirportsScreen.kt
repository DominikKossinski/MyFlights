package pl.kossa.myflights.compose.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import pl.kossa.myflights.compose.components.airport.AirportsList
import pl.kossa.myflights.fragments.airports.AirportsViewModel
import pl.kossa.myflights.navigation.AddAirportRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AirportsScreen(
    navController: NavController,
    viewModel: AirportsViewModel = hiltViewModel()
) {
    val airports by viewModel.airportsList.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Airports")
                },
                scrollBehavior = scrollBehavior,
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(AddAirportRoute)}
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null // TODO contentDescription
                )
            }
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.exclude(WindowInsets.systemBars)
    ) { innerPadding: PaddingValues ->
        AirportsList(
            navController = navController,
            airports = airports,
            contentPadding = innerPadding
        )
    }
}