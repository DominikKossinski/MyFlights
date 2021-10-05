package pl.kossa.myflights.activities.login

import android.content.Intent
import android.os.Bundle
import pl.kossa.myflights.activities.main.MainActivity
import pl.kossa.myflights.architecture.BaseActivity
import pl.kossa.myflights.databinding.ActivityLauncherBinding
import java.lang.Thread.sleep

class LauncherActivity : BaseActivity<ActivityLauncherBinding>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread {
            sleep(2000)
            openNextActivity()
        }.start()
    }

    private fun openNextActivity() {
        if (currentUser == null || !currentUser.isEmailVerified) {
            firebaseAuth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
//            refreshToken {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
//            }
        }

    }

    private fun refreshToken(onSuccess: () -> Unit) {
        firebaseAuth.currentUser?.getIdToken(true)?.addOnSuccessListener {
            preferencesHelper.token = it.token
            onSuccess()
        }?.addOnFailureListener {
            firebaseAuth.signOut()
            Intent(this, LoginActivity::class.java)
        }
    }
}
