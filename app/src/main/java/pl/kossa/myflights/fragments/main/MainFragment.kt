package pl.kossa.myflights.fragments.main

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import pl.kossa.myflights.R
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentMainBinding

@AndroidEntryPoint
class MainFragment : BaseFragment<MainViewModel, FragmentMainBinding>() {

    override val viewModel: MainViewModel by viewModels()

    override fun setOnClickListeners() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            val navController = binding.mainFragmentNavHostFragment.findNavController()
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
                R.id.actionProfile -> {
                    navController.navigate(viewModel.goToProfile())
                    true
                }
                else -> false
            }
        }
    }
}