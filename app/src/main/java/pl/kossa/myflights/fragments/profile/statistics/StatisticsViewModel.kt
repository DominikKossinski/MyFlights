package pl.kossa.myflights.fragments.profile.statistics

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import pl.kossa.myflights.api.responses.StatisticsResponse
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.repository.StatisticsRepository
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val statisticsRepository: StatisticsRepository,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    val statistics = MutableStateFlow<StatisticsResponse?>(null)

    fun fetchStatistics() {
        makeRequest {
            val response = handleRequest {
                statisticsRepository.getStatistics()
            }
            statistics.value = response
        }
    }
}