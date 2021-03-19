package pl.kossa.myflights.activities

import dagger.hilt.android.AndroidEntryPoint
import pl.kossa.myflights.R
import pl.kossa.myflights.architecture.BaseActivity

@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    override val layoutId: Int = R.layout.activity_login
}