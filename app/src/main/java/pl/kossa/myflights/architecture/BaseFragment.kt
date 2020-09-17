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
import androidx.lifecycle.observe
import pl.kossa.myflights.utils.PreferencesHelper

abstract class BaseFragment<in T : ViewDataBinding, out Y : BaseViewModel> : Fragment() {

    protected abstract val layoutId: Int

    protected abstract val viewModel: Y

    protected abstract fun setBindingVariables(binding: T)

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
            Log.d("MyLog", "Progress bar: $progressBar")
            progressBar?.isVisible = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<T>(inflater, layoutId, container, false)
        setBindingVariables(binding)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        setOnClickListeners()
    }
}