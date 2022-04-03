package pl.kossa.myflights.fragments.flights.pending.adapter

import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import pl.kossa.myflights.R
import pl.kossa.myflights.api.responses.sharedflights.SharedFlightResponse
import pl.kossa.myflights.architecture.BaseRecyclerViewAdapter
import pl.kossa.myflights.databinding.ElementPendingSharedFlightBinding
import pl.kossa.myflights.exstensions.toDateString

class PendingSharedFlightsAdapter :
    BaseRecyclerViewAdapter<SharedFlightResponse, ElementPendingSharedFlightBinding>() {

    override fun onBindViewHolder(
        holder: BaseRecyclerViewAdapter.BaseViewHolder<ElementPendingSharedFlightBinding>,
        position: Int
    ) {
        val sharedFlight = items[position]
        val flight = sharedFlight.flight
        holder.binding.routeTv.text =
            "${flight.departureAirport.icaoCode}-${flight.arrivalAirport.icaoCode}" // TODO format from resources
        holder.binding.dateTv.text = flight.arrivalDate.toDateString()

        holder.binding.nickTv.isVisible = !sharedFlight.sharedUserData?.nick.isNullOrBlank()
        holder.binding.emailTv.isVisible = !sharedFlight.sharedUserData?.email.isNullOrBlank()
        holder.binding.nickTv.text = sharedFlight.sharedUserData?.nick
        holder.binding.emailTv.text = sharedFlight.sharedUserData?.email ?: ""

        sharedFlight.sharedUserData?.avatar?.let {
            Glide.with(holder.itemView.context)
                .load(it.url)
                .transform(CircleCrop())
                .placeholder(R.drawable.ic_flight)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.binding.flightIv)
        } ?: run {
            holder.binding.flightIv.setImageResource(R.drawable.ic_flight)
            holder.binding.flightIv.setColorFilter(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.black_day_night
                )
            )
        }


    }


}