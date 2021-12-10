package pl.kossa.myflights.fragments.planned_flights.plan

import android.webkit.WebViewClient
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import pl.kossa.myflights.BuildConfig
import pl.kossa.myflights.api.responses.ApiError
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentPlanFlightBinding

@AndroidEntryPoint
class PlanFlightFragment : BaseFragment<PlanFlightViewModel, FragmentPlanFlightBinding>() {

    override val viewModel: PlanFlightViewModel by viewModels()

    override fun setOnClickListeners() {
        binding.fetchFlightButton.setOnClickListener {
            viewModel.fetchOFP()
        }
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.webViewClient = WebViewClient()
        binding.planFlightButton.setOnClickListener {
            //TODO api key
            //TODO timestamp
            binding.webView.loadUrl("https://www.simbrief.com/ofp/ofp.loader.api.php?type=a320&orig=KJFK&dest=KBOS&apicode=${BuildConfig.SIMBRIEF_API_KEY}")
        }

    }

    override fun handleApiError(apiError: ApiError) {
        TODO("Not yet implemented")
    }
}