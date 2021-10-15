package pl.kossa.myflights.views.pickers

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePicker(
    private val date: Triple<Int, Int, Int>?,
    private val onDateSet: (year: Int, month: Int, day: Int) -> Unit
) : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val year = date?.first ?: calendar.get(Calendar.YEAR)
        val month = date?.second ?: calendar.get(Calendar.MONTH)
        val day = date?.third ?: calendar.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(requireContext(), this, year, month, day).apply {
            this.datePicker.maxDate = System.currentTimeMillis()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
        onDateSet.invoke(year, month + 1, day)
    }

    companion object {
        const val TAG = "DATE_PICKER"
    }
}