package pl.kossa.myflights.architecture

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import pl.kossa.myflights.R

abstract class BaseSwipeDeleteCallback(
    context: Context
) : ItemTouchHelper.Callback() {

    private val deleteDrawable = ContextCompat.getDrawable(context, R.drawable.ic_delete)!!
    private val background = ColorDrawable()
    private val backgroundColor = ContextCompat.getColor(context, R.color.color_delete)
    private val clearPaint = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(0, ItemTouchHelper.START.or(ItemTouchHelper.END))
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false


    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val itemView = viewHolder.itemView
        val isCanceled = dX == 0f && !isCurrentlyActive
        if (isCanceled) {
            clearCanvas(
                c,
                itemView.right + dX,
                itemView.top.toFloat(),
                itemView.right.toFloat(),
                itemView.bottom.toFloat()
            )
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }
        background.color = backgroundColor
        background.setBounds(
            itemView.right + dX.toInt(),
            itemView.top,
            itemView.right,
            itemView.bottom
        )
        background.draw(c)

        val deleteTop = itemView.top + (itemView.height - deleteDrawable.intrinsicHeight) / 2
        val deleteMargin = (itemView.height - deleteDrawable.intrinsicHeight) / 2
        val deleteLeft = itemView.right - deleteMargin - deleteDrawable.intrinsicWidth
        val deleteRight = itemView.right - deleteMargin
        val deleteBottom = deleteTop + deleteDrawable.intrinsicHeight

        deleteDrawable.setBounds(deleteLeft, deleteTop, deleteRight, deleteBottom)
        deleteDrawable.draw(c)
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun clearCanvas(c: Canvas, left: Float, top: Float, right: Float, bottom: Float) {
        c.drawRect(left, top, right, bottom, clearPaint)
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return 0.7f
    }
}