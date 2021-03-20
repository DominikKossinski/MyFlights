package pl.kossa.myflights.activities.main

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import pl.kossa.myflights.R
import pl.kossa.myflights.architecture.BaseActivity

@AndroidEntryPoint
class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    override val layoutId: Int = R.layout.activity_main

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionAirplanes -> viewModel.goToAirplanes()
            R.id.actionFlights -> viewModel.goToFlights()
            R.id.actionAiports -> viewModel.goToAirports()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

}
