package pl.kossa.myflights.utils

import androidx.annotation.IdRes

enum class AlertDialogButton(@IdRes val resId: Int) {
    POSITIVE(android.R.id.button1),
    NEGATIVE(android.R.id.button2),
    NEUTRAL(android.R.id.button3)
}
