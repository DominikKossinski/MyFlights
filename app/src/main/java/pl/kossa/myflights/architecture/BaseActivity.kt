package pl.kossa.myflights.architecture

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import pl.kossa.myflights.utils.PreferencesHelper

abstract class BaseActivity : AppCompatActivity() {

    protected abstract val layoutId: Int

    protected val firebaseAuth = FirebaseAuth.getInstance()
    protected val currentUser = firebaseAuth.currentUser

    protected lateinit var preferencesHelper: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        preferencesHelper = PreferencesHelper(this)
    }

}