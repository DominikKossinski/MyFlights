package pl.kossa.myflights.fragments.airplanes.adapter

import pl.kossa.myflights.room.entities.Airplane
import pl.kossa.myflights.architecture.BaseRecyclerViewAdapter
import pl.kossa.myflights.databinding.ElementAirplaneBinding

class AirplanesAdapter : BaseRecyclerViewAdapter<Airplane, ElementAirplaneBinding>() {

    override fun onBindViewHolder(holder: BaseViewHolder<ElementAirplaneBinding>, position: Int) {
        val airplane = items[position]
        holder.binding.airplaneNameTextView.text = airplane.airplane.name
        holder.binding.root.setOnClickListener {
            onClickListener?.invoke(airplane)
        }
    }


}