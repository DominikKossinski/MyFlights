package pl.kossa.myflights.fragments.airports.runways.adapter

import pl.kossa.myflights.R
import pl.kossa.myflights.api.models.Runway
import pl.kossa.myflights.architecture.BaseRecyclerViewAdapter
import pl.kossa.myflights.databinding.ElementRunwayBinding

class RunwaysAdapter : BaseRecyclerViewAdapter<Runway, ElementRunwayBinding>() {

    override fun onBindViewHolder(holder: BaseViewHolder<ElementRunwayBinding>, position: Int) {
        val runway = items[position]
        holder.binding.nameTv.text = runway.name
        val ilsText = if (runway.ilsFrequency.isNullOrBlank()) {
            holder.itemView.context.getString(R.string.no_ils)
        } else runway.ilsFrequency
        holder.binding.ilsTv.text = ilsText
        holder.binding.root.setOnClickListener {
            onClickListener?.invoke(runway)
        }
    }
}