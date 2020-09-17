package pl.kossa.myflights.utils

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("android:error")
fun setError(textInputLayout: TextInputLayout, error: Int?) {
    if (error != null) {
        textInputLayout.error = textInputLayout.context.getString(error)
    } else {
        textInputLayout.error = null
    }
}