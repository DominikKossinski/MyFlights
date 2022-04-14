package pl.kossa.myflights.architecture.dialogs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import pl.kossa.myflights.MainNavGraphDirections
import pl.kossa.myflights.R
import pl.kossa.myflights.activities.main.MainActivity
import pl.kossa.myflights.api.responses.ApiError
import pl.kossa.myflights.architecture.BaseViewModel
import java.lang.reflect.ParameterizedType

abstract class BaseBottomSheet<VM : BaseViewModel, VB : ViewBinding> : BottomSheetDialogFragment() {

    protected lateinit var binding: VB

    protected abstract val viewModel: VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vbType = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1]
        val vbClass = vbType as Class<VB>
        val method = vbClass.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        binding = method.invoke(null, inflater, container, false) as VB
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectFlow()
        setOnClickListeners()
    }

    protected open fun setOnClickListeners() {}

    protected open fun collectFlow() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.signOutFlow.collect {
                when (findNavController().graph.id) {
                    R.id.main_nav_graph -> {
                        Navigation.findNavController(requireActivity(), R.id.mainNavHostFragment)
                            .navigate(MainNavGraphDirections.goToLoginActivity())
                    }
                    R.id.lists_nav_graph -> {
                        Navigation.findNavController(requireActivity(), R.id.mainNavHostFragment)
                            .navigate(MainNavGraphDirections.goToLoginActivity())
                    }
                }
                (activity as? MainActivity)?.finish()
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.apiErrorFlow.collect {
                it?.let { handleApiError(it) }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.backFlow.collect {
                when (findNavController().graph.id) {
                    R.id.main_nav_graph -> {
                        Navigation.findNavController(
                            requireActivity(),
                            R.id.mainNavHostFragment
                        )
                            .popBackStack()
                    }
                    R.id.lists_nav_graph -> {
                        Navigation.findNavController(
                            requireActivity(),
                            R.id.listsNavHostFragment
                        )
                            .popBackStack()
                    }
                    R.id.login_nav_graph -> {
                        Navigation.findNavController(
                            requireActivity(),
                            R.id.login_nav_host_fragment
                        ).popBackStack()
                    }
                }

            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getNavDirectionsFlow().collect {
                when (findNavController().graph.id) {
                    R.id.main_nav_graph -> {
                        Navigation.findNavController(
                            requireActivity(),
                            R.id.mainNavHostFragment
                        )
                            .navigate(it)
                    }
                    R.id.lists_nav_graph -> {
                        Navigation.findNavController(
                            requireActivity(),
                            R.id.mainNavHostFragment
                        )
                            .navigate(it)
                    }
                    R.id.login_nav_graph -> {
                        findNavController().navigate(it)
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.toastMessage.collect {
                Log.d("MyLog", "Collecting error: $it")
                it?.let {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                }
            }

        }
    }

    protected open fun handleApiError(apiError: ApiError) {
        viewModel.setToastMessage(R.string.unexpected_error)
    }

}