package pl.kossa.myflights.fragments.airports.adapter

import pl.kossa.myflights.architecture.BaseRecyclerViewAdapter
import pl.kossa.myflights.databinding.ElementAirportBinding
import pl.kossa.myflights.room.entities.Airport

class AirportsAdapter : BaseRecyclerViewAdapter<Airport, ElementAirportBinding>() {

    override fun onBindViewHolder(holder: BaseViewHolder<ElementAirportBinding>, position: Int) {
        val airport = items[position]
        holder.binding.nameTv.text = airport.airport.name
        holder.binding.icaoCodeTv.text = airport.airport.icaoCode
        holder.binding.root.setOnClickListener {
            onClickListener?.invoke(airport)
        }
    }


}