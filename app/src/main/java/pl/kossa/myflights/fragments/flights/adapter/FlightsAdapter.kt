package pl.kossa.myflights.fragments.flights.adapter

import pl.kossa.myflights.api.models.Flight
import pl.kossa.myflights.architecture.BaseRecyclerViewAdapter
import pl.kossa.myflights.databinding.ElementFlightBinding

class FlightsAdapter : BaseRecyclerViewAdapter<Flight, ElementFlightBinding>() {

    override fun onBindViewHolder(holder: BaseViewHolder<ElementFlightBinding>, position: Int) {
        val flight = items[position]
        holder.binding.root.setOnClickListener {
            onClickListener?.invoke(flight)
        }
    }
}