package pl.kossa.myflights.fragments.flights.users.adapter

import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import pl.kossa.myflights.R
import pl.kossa.myflights.api.responses.flights.ShareData
import pl.kossa.myflights.architecture.BaseRecyclerViewAdapter
import pl.kossa.myflights.databinding.ElementSharedUserBinding

class SharedUsersAdapter :
    BaseRecyclerViewAdapter<ShareData, ElementSharedUserBinding>() {

    var isMyFlight = false

    override fun onBindViewHolder(holder: BaseViewHolder<ElementSharedUserBinding>, position: Int) {
        val shareData = items[position]

        holder.binding.emailTextView.isVisible = !shareData.userData?.email.isNullOrBlank()
        holder.binding.emailTextView.text = shareData.userData?.email

        holder.binding.nickTextView.isVisible = !shareData.userData?.nick.isNullOrBlank()
        holder.binding.nickTextView.text = shareData.userData?.nick

        shareData.userData?.avatar?.url?.let {
            Glide.with(holder.itemView.context)
                .load(it)
                .transform(CircleCrop())
                .placeholder(R.drawable.ic_profile)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.binding.profileIv)
        } ?: holder.binding.profileIv.setImageResource(R.drawable.ic_profile)

        // TODO show pending flight
        // TODO show confirmation status
    }

}