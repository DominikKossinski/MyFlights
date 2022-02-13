package pl.kossa.myflights.fragments.profile.settings.about_app

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import pl.kossa.myflights.BuildConfig
import pl.kossa.myflights.api.responses.ApiError
import pl.kossa.myflights.architecture.fragments.BaseFragment
import pl.kossa.myflights.databinding.FragmentAboutAppBinding
import pl.kossa.myflights.exstensions.toDateString
import java.util.*

@AndroidEntryPoint
class AboutAppFragment : BaseFragment<AboutAppViewModel, FragmentAboutAppBinding>() {

    override val viewModel: AboutAppViewModel by viewModels()

    override fun setOnClickListeners() {
        val versionText = "${BuildConfig.VERSION_NAME}-${BuildConfig.BUILD_ENV}"
        binding.versionTextView.text = versionText
        binding.dateTextView.text = Date(BuildConfig.BUILD_TIMESTAMP).toDateString()
        binding.backAppbar.setBackOnClickListener {
            viewModel.navigateBack()
        }
    }

    override fun handleApiError(apiError: ApiError) {}
}