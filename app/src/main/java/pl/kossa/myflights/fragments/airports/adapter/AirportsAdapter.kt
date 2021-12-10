package pl.kossa.myflights.fragments.airports.adapter

import pl.kossa.myflights.api.server.models.Airport
import pl.kossa.myflights.architecture.BaseRecyclerViewAdapter
import pl.kossa.myflights.databinding.ElementAirportBinding

class AirportsAdapter : BaseRecyclerViewAdapter<Airport, ElementAirportBinding>() {

    override fun onBindViewHolder(holder: BaseViewHolder<ElementAirportBinding>, position: Int) {
        val airport = items[position]
        holder.binding.nameTv.text = airport.name
        holder.binding.icaoCodeTv.text = airport.icaoCode
        holder.binding.root.setOnClickListener {
            onClickListener?.invoke(airport)
        }
    }


}