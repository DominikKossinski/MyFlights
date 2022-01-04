package pl.kossa.myflights.activities.main

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import pl.kossa.myflights.R
import pl.kossa.myflights.architecture.BaseActivity
import pl.kossa.myflights.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val viewModel: MainActivityViewModel by viewModels()

//    override fun onNewIntent(intent: Intent?) {
//        super.onNewIntent(intent)
//        Log.d("MyLog", "Intent: $intent")
//        findNavController(R.id.listsNavHostFragment).handleDeepLink(intent)
//    }
}
