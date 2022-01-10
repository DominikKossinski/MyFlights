package pl.kossa.myflights.fragments.profile.statistics

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import pl.kossa.myflights.R
import pl.kossa.myflights.api.responses.ApiError
import pl.kossa.myflights.api.responses.StatisticsResponse
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentStatisticsBinding
import kotlin.math.roundToInt

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
        binding.flightHoursEtw.valueText = getString(R.string.double_places_float_format, statistics.flightHours)

        binding.favouriteAirplaneEtw.isVisible = statistics.favouriteAirplane != null
        binding.favouriteAirplaneEtw.valueText = statistics.favouriteAirplane?.name ?: ""

        binding.favouriteDepartureAirportEtw.isVisible =
            statistics.favouriteDepartureAirport != null
        binding.favouriteDepartureAirportEtw.valueText =
            statistics.favouriteDepartureAirport?.icaoCode ?: ""


        binding.favouriteArrivalAirportEtw.isVisible = statistics.favouriteArrivalAirport != null
        binding.favouriteArrivalAirportEtw.valueText =
            statistics.favouriteArrivalAirport?.icaoCode ?: ""
    }

    override fun handleApiError(apiError: ApiError) {
        //TODO
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchStatistics()
    }
}