package com.apkakisan.myapplication.customerservice

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import com.apkakisan.myapplication.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.apkakisan.myapplication.BaseFragment
import com.apkakisan.myapplication.databinding.FragmentCustomerServiceBinding
import com.apkakisan.myapplication.helpers.popFragment
import com.apkakisan.myapplication.profile.ProfileActivity
import com.apkakisan.myapplication.common.AppDialogFragment
import com.apkakisan.myapplication.utils.BuildTypeUtil
import com.apkakisan.myapplication.utils.KeyBoardUtil
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CustomerServiceFragment : BaseFragment() {

    private lateinit var binding: FragmentCustomerServiceBinding
    private val customerServiceViewModel by viewModel<CustomerServiceViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCustomerServiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.ibBack.setOnClickListener {
            popFragment()
        }
        binding.toolbar.tvTitle.text = getString(R.string.customer_service)

        if (BuildTypeUtil.isDebug() || BuildTypeUtil.isDebugWithRegistration())
            binding.tvCountryCode.text = "+92"

        binding.btnSave.setOnClickListener {
            customerServiceViewModel.validate()
        }

        updateUI()
        initObservers()

        binding.etName.doAfterTextChanged {
            binding.tvNameError.visibility = View.GONE
            customerServiceViewModel.nameValue = it.toString().trim()
        }

        binding.etPhoneNo.addTextChangedListener(PhoneNumberFormattingTextWatcher("US"))
        binding.etPhoneNo.doAfterTextChanged {
            binding.tvPhoneError.visibility = View.GONE
            customerServiceViewModel.phoneValue = it.toString().trim()
        }

        binding.etMessage.doAfterTextChanged {
            binding.tvMessageError.visibility = View.GONE
            customerServiceViewModel.messageValue = it.toString().trim()
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as ProfileActivity).bottomNavigation.visibility = View.GONE
    }

    private fun initObservers() {
        lifecycleScope.launch {
            // repeatOnLifecycle launches the block in a new coroutine every time the
            // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                customerServiceViewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is CustomerServiceUiState.EmptyName -> {
                            binding.tvNameError.visibility = View.VISIBLE
                            binding.tvNameError.text = getString(R.string.empty_name)
                        }
                        is CustomerServiceUiState.InvalidPhone -> {
                            binding.tvPhoneError.visibility = View.VISIBLE
                            binding.tvPhoneError.text = getString(R.string.invalid_phone)
                        }
                        is CustomerServiceUiState.EmptyPhone -> {
                            binding.tvPhoneError.visibility = View.VISIBLE
                            binding.tvPhoneError.text = getString(R.string.empty_phone)
                        }
                        is CustomerServiceUiState.EmptyMessage -> {
                            binding.tvMessageError.visibility = View.VISIBLE
                            binding.tvMessageError.text = getString(R.string.empty_message)
                        }
                        is CustomerServiceUiState.DataValidated -> {
                            customerServiceViewModel.sendMessage()
                        }
                        is CustomerServiceUiState.MessageSending -> {
                            KeyBoardUtil.hideKeyboard(requireContext(), binding.btnSave)
                            binding.loader.loader.visibility = View.VISIBLE
                        }
                        is CustomerServiceUiState.MessageSendSuccess -> {
                            binding.loader.loader.visibility = View.GONE
                            showAppDialog()
                        }
                        is CustomerServiceUiState.MessageSendFailed -> {
                            binding.loader.loader.visibility = View.GONE
                            showErrorView()
                        }
                    }
                }
            }
        }
    }

    private fun showAppDialog() {
        val dialog = AppDialogFragment.newInstance(
            getString(R.string.case_created),
            getString(R.string.case_received)
        )
        dialog.onDonePressed { popFragment() }
        dialog.show(activity?.supportFragmentManager!!, AppDialogFragment.TAG)
    }

    private fun updateUI() {
        binding.etName.setText(customerServiceViewModel.nameValue)
        binding.etPhoneNo.setText(customerServiceViewModel.phoneValue)
        binding.etMessage.setText(customerServiceViewModel.messageValue)
    }

    companion object {
        const val TAG = "CustomerServiceFragment"
        fun newInstance() = CustomerServiceFragment()
    }
}