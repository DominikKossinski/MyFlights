package pl.kossa.myflights.architecture.fragments

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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import pl.kossa.myflights.MainNavGraphDirections
import pl.kossa.myflights.R
import pl.kossa.myflights.activities.main.MainActivity
import pl.kossa.myflights.api.responses.ApiError
import pl.kossa.myflights.architecture.BaseViewModel
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
        collectFlow()
    }

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
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.backFlow.collect {
                Log.d("MyLog", "Collecting back navigation")
                when (findNavController().graph.id) {
                    R.id.main_nav_graph -> {
                        Navigation.findNavController(
                            requireActivity(),
                            R.id.mainNavHostFragment
                        ).popBackStack()
                    }
                    R.id.lists_nav_graph -> {
                        Navigation.findNavController(
                            requireActivity(),
                            R.id.listsNavHostFragment
                        ).popBackStack()
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
            viewModel.activityFinishFlow.collect {
                requireActivity().finish()
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getNavDirectionsFlow().collect {
                Log.d("MyLog", "Collecting navigation")
                Log.d("MyLog", "${findNavController().currentDestination?.label}")
                when (findNavController().graph.id) {
                    R.id.main_nav_graph -> {
                        Log.d("MyLog", "MainGraph $it")
                        Navigation.findNavController(
                            requireActivity(),
                            R.id.mainNavHostFragment
                        )
                            .navigate(it)
                    }
                    R.id.lists_nav_graph -> {
                        Log.d("MyLog", "ListGraph $it")
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
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isLoadingData.collect {
                progressBar?.isVisible = it
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.apiErrorFlow.collect {
                it?.let { handleApiError(it) }
            }
        }
    }

    protected abstract fun handleApiError(apiError: ApiError)
}