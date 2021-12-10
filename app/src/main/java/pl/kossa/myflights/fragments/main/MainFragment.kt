package pl.kossa.myflights.fragments.main

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pl.kossa.myflights.R
import pl.kossa.myflights.api.responses.ApiError
import pl.kossa.myflights.api.responses.HttpCode
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentMainBinding
import pl.kossa.myflights.exstensions.md5Simbrief

@AndroidEntryPoint
class MainFragment : BaseFragment<MainViewModel, FragmentMainBinding>() {

    override val viewModel: MainViewModel by viewModels()

    override fun setOnClickListeners() {
        lifecycleScope.launch {
            Log.d("MyLog", "Md5: ${md5Simbrief("KJFKKBOSa320")}")
        }
        binding.logoAppBar.setOnProfileClickListener {
            viewModel.navigateToProfile()
        }
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            val navController = binding.listsNavHostFragment.findNavController()
            return@setOnNavigationItemSelectedListener when (it.itemId) {
                R.id.actionAirplanes -> {
                    navController.navigate(viewModel.goToAirplanes())
                    true
                }
                R.id.actionFlights -> {
                    navController.navigate(viewModel.goToFlights())
                    true
                }
                R.id.actionAirports -> {
                    navController.navigate(viewModel.goToAirports())
                    true
                }
                R.id.actionPlanedFlights -> {
                    viewModel.showComingSoonDialog()
                    false
                }
                else -> false
            }
        }
    }

    override fun handleApiError(apiError: ApiError) {
        when (apiError.code) {
            HttpCode.INTERNAL_SERVER_ERROR.code -> {
                viewModel.setToastMessage(R.string.unexpected_error)
            }
            HttpCode.FORBIDDEN.code -> {
                viewModel.setToastMessage(R.string.error_forbidden)
            }
            else -> {
                viewModel.setToastMessage(R.string.unexpected_error)
            }
        }
    }
}