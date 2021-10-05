package pl.kossa.myflights.activities.main

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import pl.kossa.myflights.R
import pl.kossa.myflights.architecture.BaseActivity
import pl.kossa.myflights.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(), NavigationView.OnNavigationItemSelectedListener {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//   TODO     navView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionAirplanes -> viewModel.goToAirplanes()
            R.id.actionFlights -> viewModel.goToFlights()
            R.id.actionAirports -> viewModel.goToAirports()
        }
// TODO       drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

}
