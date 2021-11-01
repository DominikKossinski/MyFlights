package pl.kossa.myflights.exstensions

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.loader.content.CursorLoader


fun Uri.getFilePath(context: Context): String? {
    val projection: Array<String> = arrayOf(MediaStore.MediaColumns.DATA)
    val cursor = context.contentResolver.query(this, projection, null, null, null)
    val column_index = cursor?.getColumnIndex(MediaStore.MediaColumns.DATA)
    cursor?.moveToFirst()
    Log.d("MyLog", "Column index $column_index")
    return column_index?.let {
        val a = cursor.getString(column_index)
        Log.d("MyLog", "Path $a")
        a
    }
}