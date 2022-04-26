package pl.kossa.myflights.fragments.flights.scan

import android.net.Uri
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import dagger.hilt.android.AndroidEntryPoint
import pl.kossa.myflights.R
import pl.kossa.myflights.api.responses.ApiError
import pl.kossa.myflights.architecture.fragments.BaseFragment
import pl.kossa.myflights.databinding.FragmentScanQrCodeBinding
import java.net.URLDecoder
import java.util.concurrent.Executors

@AndroidEntryPoint
@ExperimentalGetImage
class ScanQRCodeFragment : BaseFragment<ScanQRCodeViewModel, FragmentScanQrCodeBinding>() {

    override val viewModel: ScanQRCodeViewModel by viewModels()

    private val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_QR_CODE,
            Barcode.FORMAT_AZTEC
        )
        .build()

    private val barcodeScanner = BarcodeScanning.getClient(options)


    private val analysisUseCase by lazy {
        ImageAnalysis.Builder()
            .setTargetRotation(binding.qrCodeSv.display.rotation)
            .build()
    }

    private val executor = Executors.newSingleThreadExecutor()

    private val cameraSelector by lazy {
        CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
    }

    private val previewUseCase by lazy {
        Preview.Builder()
            .setTargetRotation(binding.qrCodeSv.display.rotation)
            .build()
    }


    private val cameraProviderFuture by lazy {
        ProcessCameraProvider.getInstance(requireActivity().application)
    }


    override fun setOnClickListeners() {
        binding.backAppbar.setBackOnClickListener {
            viewModel.navigateBack()
        }
        previewUseCase.setSurfaceProvider(binding.qrCodeSv.surfaceProvider)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            cameraProvider.unbind(previewUseCase)
            cameraProvider.unbind(analysisUseCase)
            cameraProvider.bindToLifecycle(viewLifecycleOwner, cameraSelector, previewUseCase)
            cameraProvider.bindToLifecycle(viewLifecycleOwner, cameraSelector, analysisUseCase)
        }, ContextCompat.getMainExecutor(requireContext()))

        analysisUseCase.setAnalyzer(
            executor
        ) { imageProxy: ImageProxy ->
            processImagePoxy(imageProxy)
        }
    }

    override fun handleApiError(apiError: ApiError) {
        TODO("Not yet implemented")
    }

    private fun processImagePoxy(imageProxy: ImageProxy) {
        val image = imageProxy.image ?: return
        val inputImage = InputImage.fromMediaImage(image, imageProxy.imageInfo.rotationDegrees)
        barcodeScanner.process(inputImage)
            .addOnSuccessListener { barcodes ->
                barcodes.firstOrNull()?.displayValue?.let { value ->
                    val uri = Uri.parse(value)
                    val link = Uri.parse(URLDecoder.decode(uri.getQueryParameter("link"), "utf-8"))
                    val sharedFlightId = link.getQueryParameter("sharedFlightId")
                    sharedFlightId?.let { viewModel.navigateToJoinSharedFlight(sharedFlightId) }
                }
            }
            .addOnFailureListener {
                viewModel.setToastMessage(R.string.scan_qr_code_error)
                viewModel.navigateBack()
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }

}