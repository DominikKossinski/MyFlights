package pl.kossa.myflights.fragments.profile.statistics

import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import pl.kossa.myflights.R
import pl.kossa.myflights.api.responses.ApiError
import pl.kossa.myflights.api.responses.StatisticsResponse
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentStatisticsBinding
import pl.kossa.myflights.exstensions.setupStatsChart
import pl.kossa.myflights.utils.charts.IntValueFormatter
import pl.kossa.myflights.utils.charts.StringLabelsFormatter

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

    private fun setupStatisticsData(statistics: StatisticsResponse) {
        binding.flightHoursEtw.valueText =
            getString(R.string.double_places_float_format, statistics.flightHours)

        binding.favouriteAirplaneEtw.isVisible = statistics.favouriteAirplane != null
        binding.favouriteAirplaneEtw.valueText = statistics.favouriteAirplane?.name ?: ""


        val airplanesEntries = statistics.top5Airplanes.mapIndexed { index, element ->
            BarEntry(index.toFloat(), element.occurrences.toFloat())
        }
        val airplanesNames = statistics.top5Airplanes.map { it.item.name }
        binding.topAirplanesBc.setupStatsChart(airplanesEntries, airplanesNames)


        binding.favouriteDepartureAirportEtw.isVisible =
            statistics.favouriteDepartureAirport != null
        binding.favouriteDepartureAirportEtw.valueText =
            statistics.favouriteDepartureAirport?.icaoCode ?: ""
        val departuresEntries = statistics.top5DepartureAirports.mapIndexed { index, element ->
            BarEntry(index.toFloat(), element.occurrences.toFloat())
        }
        val departuresNames = statistics.top5DepartureAirports.map { it.item.icaoCode }
        binding.topDeparturesBc.setupStatsChart(departuresEntries, departuresNames)


        binding.favouriteArrivalAirportEtw.isVisible = statistics.favouriteArrivalAirport != null
        binding.favouriteArrivalAirportEtw.valueText =
            statistics.favouriteArrivalAirport?.icaoCode ?: ""
        val arrivalsEntries = statistics.top5ArrivalAirports.mapIndexed { index, element ->
            BarEntry(index.toFloat(), element.occurrences.toFloat())
        }
        val arrivalsNames = statistics.top5ArrivalAirports.map { it.item.icaoCode }
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