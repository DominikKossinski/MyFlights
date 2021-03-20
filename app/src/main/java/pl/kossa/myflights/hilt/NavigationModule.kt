package pl.kossa.myflights.hilt

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent


@InstallIn(FragmentComponent::class)
@Module
object NavigationModule {

//    @Provides
//    fun providesNavigationModule(fragment: Fragment): NavController {
//        return fragment.findNavController()
//    }
}