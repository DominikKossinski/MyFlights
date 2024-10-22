package pl.kossa.myflights.compose.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.navigation.AirportRoute
import pl.kossa.myflights.repository.AirportRepository
import pl.kossa.myflights.room.entities.Airport
import pl.kossa.myflights.utils.PreferencesHelper

@HiltViewModel(assistedFactory = AirportViewModel.AirportViewModelFactory::class)
class AirportViewModel @AssistedInject constructor(
    @Assisted val airportRoute: AirportRoute,
    private val airportRepository: AirportRepository,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    val airport = MutableStateFlow<Airport?>(null)

    init {
        fetchAirport()
    }


    fun fetchAirport() {
        makeRequest {
            airport.value = handleRequest {
                airportRepository.getAirportById(airportRoute.id)
            }
        }
    }

    @AssistedFactory
    interface AirportViewModelFactory {
        fun create(airportRoute: AirportRoute): AirportViewModel
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AirportScreen(
    navController: NavController,
    airportRoute: AirportRoute,
    viewModel: AirportViewModel = hiltViewModel<AirportViewModel, AirportViewModel.AirportViewModelFactory> { factory ->
        factory.create(airportRoute = airportRoute)
    },
    showSnackbar: (String) -> Unit = {}
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Text(airportRoute.icaoCode)
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null // TODO
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            // TODO GO to delete
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = null // TODO
                        )
                    }
                    IconButton(
                        onClick = {
                            // TODO GO to delete
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = null // TODO
                        )
                    }
                }
            )
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.exclude(WindowInsets.systemBars),
    ) { innerPadding: PaddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues = innerPadding)
        ) {
            Text("AirportId: ${viewModel.airportRoute.id}")
            val airport by viewModel.airport.collectAsState()
            Text("$airport")
            Button(onClick = {
                showSnackbar.invoke("test")
                navController.popBackStack()
            }) {
                Text("Test")
            }
        }
    }

}

@Composable
@Preview
private fun AirportScreenPreview() {
    val route = AirportRoute("Abc", "abc")
//    AirportScreen(
//        airportRoute = route,
//        viewModel = AirportViewModel(airportRoute = route)
//    )
}