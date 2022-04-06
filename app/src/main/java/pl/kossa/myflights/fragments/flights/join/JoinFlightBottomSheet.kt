package pl.kossa.myflights.fragments.flights.join

import androidx.fragment.app.viewModels
import pl.kossa.myflights.architecture.dialogs.BaseBottomSheet
import pl.kossa.myflights.databinding.DialogJoinFlightBinding

class JoinFlightBottomSheet: BaseBottomSheet<JoinFlightViewModel, DialogJoinFlightBinding>() {

    override val viewModel: JoinFlightViewModel by viewModels()

    override fun setOnClickListeners() {
        super.setOnClickListeners()
        // TODO
    }

    override fun collectFlow() {
        super.collectFlow()

    }
}