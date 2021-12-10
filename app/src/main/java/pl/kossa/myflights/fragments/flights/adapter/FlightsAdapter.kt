package pl.kossa.myflights.fragments.flights.adapter

import pl.kossa.myflights.api.server.models.Flight
import pl.kossa.myflights.architecture.BaseRecyclerViewAdapter
import pl.kossa.myflights.databinding.ElementFlightBinding
import pl.kossa.myflights.exstensions.toDateString

class FlightsAdapter : BaseRecyclerViewAdapter<Flight, ElementFlightBinding>() {

    override fun onBindViewHolder(holder: BaseViewHolder<ElementFlightBinding>, position: Int) {
        val flight = items[position]
        holder.binding.routeTv.text =
            "${flight.departureAirport.icaoCode}-${flight.arrivalAirport.icaoCode}" // TODO format from resources
        holder.binding.dateTv.text = flight.arrivalDate.toDateString()
        holder.binding.root.setOnClickListener {
            onClickListener?.invoke(flight)
        }
    }
}