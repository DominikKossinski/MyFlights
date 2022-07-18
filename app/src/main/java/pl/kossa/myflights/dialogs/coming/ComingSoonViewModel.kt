package pl.kossa.myflights.dialogs.coming

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class ComingSoonViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    val textFlow = MutableStateFlow(savedStateHandle.get<String?>("text"))
}