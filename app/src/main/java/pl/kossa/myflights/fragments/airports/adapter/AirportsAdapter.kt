package pl.kossa.myflights.fragments.airports.adapter

import pl.kossa.myflights.api.models.Airport
import pl.kossa.myflights.architecture.BaseRecyclerViewAdapter
import pl.kossa.myflights.databinding.ElementAirplaneBinding
import pl.kossa.myflights.databinding.ElementAirportBinding

class AirportsAdapter : BaseRecyclerViewAdapter<Airport, ElementAirportBinding>() {

    override fun onBindViewHolder(holder: BaseViewHolder<ElementAirportBinding>, position: Int) {
        val airport = items[position]
        holder.binding.airportNameTextView.text = airport.name
        holder.binding.cityTextView.text = airport.city
        holder.binding.root.setOnClickListener {
            onClickListener?.invoke(airport)
        }
    }


}