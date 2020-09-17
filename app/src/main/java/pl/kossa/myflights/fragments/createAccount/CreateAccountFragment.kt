package pl.kossa.myflights.fragments.createAccount

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_create_account.*
import pl.kossa.myflights.R
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentCreateAccountBinding

class CreateAccountFragment : BaseFragment<FragmentCreateAccountBinding, CreateAccountViewModel>() {

    override val layoutId: Int = R.layout.fragment_create_account

    override val viewModel: CreateAccountViewModel by lazy {
        CreateAccountViewModel(findNavController(), preferencesHelper)
    }

    override fun setBindingVariables(binding: FragmentCreateAccountBinding) {
        binding.viewModel = viewModel
    }

    override fun setOnClickListeners() {
        backButton.setOnClickListener {
            viewModel.navigateBack()
        }
        createAccountButton.setOnClickListener {
            viewModel.createAccount()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = loadingProgressBar
    }


}