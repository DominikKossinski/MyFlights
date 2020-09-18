package pl.kossa.myflights.architecture

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseBindingRvAdapter<T> : RecyclerView.Adapter<BaseBindingViewHolder<T>>() {

    abstract val layoutId: Int

    val items = arrayListOf<BaseRecyclerViewModel<T>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder<T> {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, layoutId, parent, false)
        return BaseBindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<T>, position: Int) {
        val model = items[position]
        holder.bind(model)
    }

    override fun getItemCount(): Int = items.size

}