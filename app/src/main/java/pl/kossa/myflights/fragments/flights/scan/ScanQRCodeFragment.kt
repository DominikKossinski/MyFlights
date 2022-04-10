package pl.kossa.myflights.fragments.flights.scan

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import pl.kossa.myflights.api.responses.ApiError
import pl.kossa.myflights.architecture.fragments.BaseFragment
import pl.kossa.myflights.databinding.FragmentScanQrCodeBinding

@AndroidEntryPoint
class ScanQRCodeFragment: BaseFragment<ScanQRCodeViewModel, FragmentScanQrCodeBinding>() {

    override val viewModel: ScanQRCodeViewModel by viewModels()

    override fun setOnClickListeners() {
        //TODO
    }

    override fun handleApiError(apiError: ApiError) {
        TODO("Not yet implemented")
    }
}