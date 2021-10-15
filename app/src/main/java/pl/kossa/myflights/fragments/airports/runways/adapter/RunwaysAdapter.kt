package pl.kossa.myflights.fragments.airports.runways.adapter

import pl.kossa.myflights.api.models.Runway
import pl.kossa.myflights.architecture.BaseRecyclerViewAdapter
import pl.kossa.myflights.databinding.ElementRunwayBinding

class RunwaysAdapter : BaseRecyclerViewAdapter<Runway, ElementRunwayBinding>() {

    override fun onBindViewHolder(holder: BaseViewHolder<ElementRunwayBinding>, position: Int) {
        val runway = items[position]
        holder.binding.nameTv.text = runway.name
        holder.binding.ilsTv.text = runway.ilsFrequency
        holder.binding.root.setOnClickListener {
            onClickListener?.invoke(runway)
        }
    }
}