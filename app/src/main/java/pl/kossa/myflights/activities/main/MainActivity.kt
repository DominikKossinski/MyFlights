package pl.kossa.myflights.activities.main

import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import pl.kossa.myflights.architecture.BaseActivity
import pl.kossa.myflights.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val viewModel: MainActivityViewModel by viewModels()
}
