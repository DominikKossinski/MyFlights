package pl.kossa.myflights.activities.main

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import pl.kossa.myflights.R
import pl.kossa.myflights.architecture.BaseActivity

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    override val layoutId: Int = R.layout.activity_main

    private val viewModel by lazy {
        MainActivityViewModel(
            findNavController(R.id.mainNavHostFragment),
            preferencesHelper
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fab.setOnClickListener { onFabClicked() }
        navView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionAirplanes -> viewModel.goToAirplanes()
            R.id.actionFlights -> viewModel.goToFlights()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun onFabClicked() {
        Log.d("MyLog", "Nav:")
        when (viewModel.navController.currentDestination?.id) {
            R.id.airplanesFragment -> {
                viewModel.goToAirplaneAdd()
                Log.d("MyLog", "Nav: after")
            }
        }
    }
}
