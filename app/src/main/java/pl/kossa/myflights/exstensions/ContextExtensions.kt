package pl.kossa.myflights.exstensions

import android.content.Context

fun Context.dpToPx(dp: Float): Float {
    val density = this.resources.displayMetrics.density
    return dp * density
}