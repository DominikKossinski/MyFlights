package pl.kossa.myflights.utils.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import okhttp3.OkHttpClient
import pl.kossa.myflights.utils.PreferencesHelper
import java.io.InputStream

@GlideModule
class MyFlightsGlideModule : AppGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val preferences =
                context.applicationContext.getSharedPreferences(
                    PreferencesHelper.PREFERENCES,
                    Context.MODE_PRIVATE
                )
            val token = preferences.getString(PreferencesHelper.TOKEN, "")
            val newRequest = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(newRequest)
        }.build()
        super.registerComponents(context, glide, registry)
        glide.registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(client)
        )
    }
}