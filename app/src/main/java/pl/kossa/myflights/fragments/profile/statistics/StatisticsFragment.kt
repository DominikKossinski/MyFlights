package pl.kossa.myflights.fragments.profile.statistics

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.data.BarEntry
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import okhttp3.internal.notifyAll
import pl.kossa.myflights.R
import pl.kossa.myflights.api.responses.ApiError
import pl.kossa.myflights.api.responses.StatisticsResponse
import pl.kossa.myflights.architecture.fragments.BaseFragment
import pl.kossa.myflights.databinding.FragmentStatisticsBinding
import pl.kossa.myflights.room.entities.Statistics

@AndroidEntryPoint
class StatisticsFragment : BaseFragment<StatisticsViewModel, FragmentStatisticsBinding>() {

    override val viewModel: StatisticsViewModel by viewModels()

    override fun setOnClickListeners() {
        binding.backAppbar.setBackOnClickListener {
            viewModel.navigateBack()
        }
        binding.statisticsSwipeRefresh.setOnRefreshListener {
            viewModel.fetchStatistics()
        }
    }


    override fun collectFlow() {
        super.collectFlow()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.statistics.collectLatest {
                it?.let { setupStatisticsData(it) }
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isLoadingData.collectLatest {
                binding.statisticsSwipeRefresh.isRefreshing = it
            }
        }
    }

    private fun setupStatisticsData(statistics: Statistics) {
        binding.flightHoursEtw.valueText =
            getString(R.string.double_places_float_format, statistics.statistics.flightHours)

        binding.favouriteAirplaneEtw.isVisible = statistics.favouriteAirplane != null
        binding.favouriteAirplaneEtw.valueText = statistics.favouriteAirplane?.airplane?.name ?: ""


        val airplanesEntries = statistics.top5Airplanes.mapIndexed { index, element ->
            BarEntry(index.toFloat(), element.topNAirplane.occurrences.toFloat())
        }
        val airplanesNames = statistics.top5Airplanes.map { it.airplane.airplane.name }
        binding.topAirplanesBc.setupStatsChart(airplanesEntries, airplanesNames)


        binding.favouriteDepartureAirportEtw.isVisible =
            statistics.favouriteDepartureAirport != null
        binding.favouriteDepartureAirportEtw.valueText =
            statistics.favouriteDepartureAirport?.airport?.icaoCode ?: ""
        val departuresEntries = statistics.top5DepartureAirports.mapIndexed { index, element ->
            BarEntry(index.toFloat(), element.topNAirport.occurrences.toFloat())
        }
        val departuresNames = statistics.top5DepartureAirports.map { it.airport.airport.icaoCode }
        binding.topDeparturesBc.setupStatsChart(departuresEntries, departuresNames)


        binding.favouriteArrivalAirportEtw.isVisible = statistics.favouriteArrivalAirport != null
        binding.favouriteArrivalAirportEtw.valueText =
            statistics.favouriteArrivalAirport?.airport?.icaoCode ?: ""
        val arrivalsEntries = statistics.top5ArrivalAirports.mapIndexed { index, element ->
            BarEntry(index.toFloat(), element.topNAirport.occurrences.toFloat())
        }
        val arrivalsNames = statistics.top5ArrivalAirports.map { it.airport.airport.icaoCode }
        binding.topArrivalsBc.setupStatsChart(arrivalsEntries, arrivalsNames)

    }

    override fun handleApiError(apiError: ApiError) {
        //TODO
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchStatistics()
    }
}