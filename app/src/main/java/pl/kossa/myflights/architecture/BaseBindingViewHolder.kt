package pl.kossa.myflights.architecture

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import pl.kossa.myflights.BR

class BaseBindingViewHolder<T>(
    private val binding: ViewDataBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: T) {
        binding.setVariable(BR.model, model)
        binding.executePendingBindings()
    }
}