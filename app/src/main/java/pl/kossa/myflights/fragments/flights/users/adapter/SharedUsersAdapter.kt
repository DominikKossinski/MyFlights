package pl.kossa.myflights.fragments.flights.users.adapter

import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import pl.kossa.myflights.R
import pl.kossa.myflights.architecture.BaseRecyclerViewAdapter
import pl.kossa.myflights.databinding.ElementSharedUserBinding
import pl.kossa.myflights.room.entities.ShareData

class SharedUsersAdapter : BaseRecyclerViewAdapter<ShareData, ElementSharedUserBinding>() {

    var isMyFlight = false

    override fun onBindViewHolder(holder: BaseViewHolder<ElementSharedUserBinding>, position: Int) {
        val shareData = items[position]

        holder.binding.emailTextView.isVisible =
            !shareData.sharedUserData?.sharedUser?.email.isNullOrBlank()
        holder.binding.emailTextView.text = shareData.sharedUserData?.sharedUser?.email

        holder.binding.nickTextView.isVisible =
            !shareData.sharedUserData?.sharedUser?.nick.isNullOrBlank()
        holder.binding.nickTextView.text = shareData.sharedUserData?.sharedUser?.nick

        shareData.sharedUserData?.image?.url?.let {
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