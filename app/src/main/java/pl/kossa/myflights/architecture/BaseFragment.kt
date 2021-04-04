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
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import pl.kossa.myflights.MainNavGraphDirections
import pl.kossa.myflights.R
import pl.kossa.myflights.utils.PreferencesHelper
import pl.kossa.myflights.BR

abstract class BaseFragment<out Y : BaseViewModel> : Fragment() {

    protected abstract val layoutId: Int

    protected abstract val viewModel: Y

    protected abstract fun setOnClickListeners()

    protected var progressBar: ProgressBar? = null


    protected val preferencesHelper by lazy {
        PreferencesHelper(requireActivity() as AppCompatActivity)
    }

    protected open fun setObservers() {
        viewModel.toastError.observe(viewLifecycleOwner) {
            if (it != null) {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
        viewModel.isLoadingData.observe(viewLifecycleOwner) {
            progressBar?.isVisible = it
        }
        viewModel.navDirectionLiveData.observe(viewLifecycleOwner) {
            findNavController().navigate(it)
        }
        viewModel.backLiveData.observe(viewLifecycleOwner) {
            Log.d("MyLog", "Go back")
            findNavController().popBackStack()
        }
        viewModel.signOutLiveData.observe(viewLifecycleOwner) {
            when (findNavController().graph.id) {
                R.navigation.main_nav_graph -> {
                    findNavController().navigate(MainNavGraphDirections.goToLoginActivity())
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, layoutId, container, false)
        binding.setVariable(BR.viewModel, viewModel)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        setOnClickListeners()
    }
}