package pl.kossa.myflights.exstensions

import android.widget.EditText
import androidx.core.widget.doOnTextChanged

fun EditText.doOnTextChanged(listener: (String) -> Unit) {
    this.doOnTextChanged { text, _, _, _ ->
        listener.invoke(text.toString())
    }
}