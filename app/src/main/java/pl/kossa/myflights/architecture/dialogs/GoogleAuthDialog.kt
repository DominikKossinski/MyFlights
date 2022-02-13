package pl.kossa.myflights.architecture.dialogs

import android.app.Activity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.viewbinding.ViewBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import pl.kossa.myflights.R
import pl.kossa.myflights.architecture.BaseViewModel

abstract class GoogleAuthDialog<VM : BaseViewModel, VB : ViewBinding> : BaseDialog<VM, VB>() {

    private val googleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }

    private val googleSignInActivityLauncher = this.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val accountTask = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            accountTask.addOnSuccessListener {
                onGoogleAuthSuccess(it)
            }.addOnFailureListener {
                viewModel.setToastMessage(R.string.error_sign_in_with_google)
            }
        } else {
            viewModel.setToastMessage(R.string.error_sign_in_with_google)
        }
    }

    private val googleSignInClient by lazy {
        GoogleSignIn.getClient(requireActivity(), googleSignInOptions)
    }

    protected fun launchGoogleSignInIntent() {
        val intent = googleSignInClient.signInIntent
        googleSignInActivityLauncher.launch(intent)
    }

    protected abstract fun onGoogleAuthSuccess(account: GoogleSignInAccount)

}