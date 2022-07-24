package pl.kossa.myflights.utils.links

import android.content.Context
import android.net.Uri
import com.google.firebase.dynamiclinks.ShortDynamicLink
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.shortLinkAsync
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import pl.kossa.myflights.R

class DynamicLinksResolver(
    private val context: Context
) {

    fun getSharedFlightDynamicLink(sharedFlightId: String): Flow<Result<String?>?> {
        val appLink = context.resources.getString(R.string.share_flight_uri_format, sharedFlightId)
        val flow = MutableStateFlow<Result<String?>?>(null)
        Firebase.dynamicLinks.shortLinkAsync(ShortDynamicLink.Suffix.SHORT) {
            link = Uri.parse(appLink)
            domainUriPrefix = context.resources.getString(R.string.dynamic_link_prefix)
            androidParameters(context.packageName) {
                minimumVersion = 1
            }
        }.addOnSuccessListener {
            flow.value = Result.success(it.shortLink?.toString())
        }.addOnFailureListener {
            flow.value = Result.failure(it)
        }
        return flow
    }
}