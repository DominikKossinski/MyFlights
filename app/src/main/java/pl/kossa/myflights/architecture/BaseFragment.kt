package pl.kossa.myflights.architecture

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pl.kossa.myflights.MainNavGraphDirections
import pl.kossa.myflights.R
import pl.kossa.myflights.activities.main.MainActivity
import pl.kossa.myflights.api.responses.ApiError
import pl.kossa.myflights.utils.PreferencesHelper
import java.lang.reflect.ParameterizedType

abstract class BaseFragment<VM : BaseViewModel, VB : ViewBinding> : Fragment() {

    protected abstract val viewModel: VM

    protected lateinit var binding: VB

    protected abstract fun setOnClickListeners()

    protected var progressBar: ProgressBar? = null


    protected val preferencesHelper by lazy {
        PreferencesHelper(requireActivity() as AppCompatActivity)
    }

    protected open fun setObservers() {
        viewModel.navDirectionLiveData.observe(viewLifecycleOwner) {
            when (findNavController().graph.id) {
                R.id.main_nav_graph -> {
                    Navigation.findNavController(requireActivity(), R.id.mainNavHostFragment)
                        .navigate(it)
                }
                R.id.lists_nav_graph -> {
                    Navigation.findNavController(requireActivity(), R.id.mainNavHostFragment)
                        .navigate(it)
                }
                R.id.login_nav_graph -> {
                    findNavController().navigate(it)
                }
            }
        }
        viewModel.backLiveData.observe(viewLifecycleOwner) {
            when (findNavController().graph.id) {
                R.id.main_nav_graph -> {
                    Navigation.findNavController(requireActivity(), R.id.mainNavHostFragment)
                        .popBackStack()
                }
                R.id.lists_nav_graph -> {
                    Navigation.findNavController(requireActivity(), R.id.listsNavHostFragment)
                        .popBackStack()
                }
                R.id.login_nav_graph -> {
                    Navigation.findNavController(requireActivity(), R.id.login_nav_host_fragment)
                        .popBackStack()
                }
            }
        }
        viewModel.signOutLiveData.observe(viewLifecycleOwner) {
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        setOnClickListeners()
        setObservers()
        collectFlow()
    }

    protected open fun collectFlow() {
        lifecycleScope.launch {
            viewModel.toastError.collect {
                Log.d("MyLog", "Collecting error: $it")
                it?.let {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                }
            }

        }
        lifecycleScope.launch {
            viewModel.isLoadingData.collect {
                progressBar?.isVisible = it
            }
        }
        lifecycleScope.launch {
            viewModel.apiErrorFlow.collect {
                it?.let { handleApiError(it) }
            }
        }
    }

    protected abstract fun handleApiError(apiError: ApiError)
}