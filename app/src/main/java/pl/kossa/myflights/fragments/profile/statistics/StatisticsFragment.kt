package pl.kossa.myflights.fragments.profile.statistics

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentStatisticsBinding

@AndroidEntryPoint
class StatisticsFragment : BaseFragment<StatisticsViewModel, FragmentStatisticsBinding>() {

    override val viewModel: StatisticsViewModel by viewModels()

    override fun setOnClickListeners() {
        //TODO
    }
}