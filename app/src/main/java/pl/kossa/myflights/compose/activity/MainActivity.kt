package pl.kossa.myflights.compose.activity

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.sharp.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import pl.kossa.myflights.compose.screens.AddAirportScreen
import pl.kossa.myflights.compose.screens.AirportScreen
import pl.kossa.myflights.compose.screens.AirportsScreen
import pl.kossa.myflights.navigation.AddAirportRoute
import pl.kossa.myflights.navigation.AirportRoute
import pl.kossa.myflights.navigation.AirportsRoute
import pl.kossa.myflights.navigation.TopLevelRoute

@Serializable
object ProfileRoute


@Serializable
object FlightsRoute

@Serializable
object AirplanesRoute


@Composable
fun MainActivity() {
    val topRoutes = listOf(
        TopLevelRoute("Flights", FlightsRoute, Icons.Sharp.Done),
        TopLevelRoute("Airplanes", AirplanesRoute, Icons.Sharp.Done),
        TopLevelRoute("Airports", AirportsRoute, Icons.Sharp.Done),
        TopLevelRoute("Profile", ProfileRoute, Icons.Filled.Person)
    )
    var navigationSelectedItem by remember {
        mutableIntStateOf(0)
    }
    val navController = rememberNavController()
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val snackbarScope = rememberCoroutineScope()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        },
        bottomBar = {

            NavigationBar {
                topRoutes.mapIndexed { index: Int, topLevelRoute: TopLevelRoute<*> ->
                    NavigationBarItem(
                        selected = index == navigationSelectedItem,
                        label = {
                            Text(topLevelRoute.name)
                        },
                        icon = {
                            Icon(
                                topLevelRoute.icon,
                                contentDescription = topLevelRoute.name
                            )
                        },
                        onClick = {
                            navController.navigate(topLevelRoute.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                            navigationSelectedItem = index
                        }
                    )
                }
            }
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.exclude(WindowInsets.systemBars),
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ProfileRoute,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<FlightsRoute> {
                Text("Flights")
            }
            composable<AirplanesRoute> {
                Text("Airplanes")
            }
            composable<AirportsRoute> {
                AirportsScreen(
                    navController = navController
                )
            }
            composable<ProfileRoute> {
                Text("Profile")
            }


            composable<AddAirportRoute> {
                AddAirportScreen()
            }

            composable<AirportRoute> { backStackEntry ->
                val airportRoute = backStackEntry.toRoute<AirportRoute>()
                AirportScreen(
                    navController = navController,
                    airportRoute = airportRoute,
                    showSnackbar = { text ->
                        snackbarScope.launch {
                            snackbarHostState.showSnackbar(text)
                        }
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun MainActivityScreenPreview() {
    MainActivity()
}