package pl.kossa.myflights.fragments.launcher

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import pl.kossa.myflights.R
import pl.kossa.myflights.api.responses.ApiError
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentLauncherBinding
import java.lang.Thread.sleep

@AndroidEntryPoint
class LauncherFragment: BaseFragment<LauncherViewModel, FragmentLauncherBinding>() {

    override val viewModel: LauncherViewModel  by viewModels()

    override fun setOnClickListeners() {
        Thread {
            sleep(2000)
            viewModel.openNextActivity()
        }.start()
    }

    override fun handleApiError(apiError: ApiError) {
        viewModel.setToastMessage(R.string.unexpected_error)
    }
}