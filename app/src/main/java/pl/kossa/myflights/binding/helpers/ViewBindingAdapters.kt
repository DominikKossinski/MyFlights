package pl.kossa.myflights.binding.helpers

import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

@BindingAdapter("android:visibility")
fun setVisibility(view: View, isVisible: Boolean) {
    view.isVisible = isVisible
}