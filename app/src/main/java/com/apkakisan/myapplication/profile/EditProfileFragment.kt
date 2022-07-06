package com.apkakisan.myapplication.profile

import android.app.Activity
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.apkakisan.myapplication.BaseFragment
import com.apkakisan.myapplication.R
import com.apkakisan.myapplication.common.AppDialogFragment
import com.apkakisan.myapplication.databinding.FragmentEditProfileBinding
import com.apkakisan.myapplication.helpers.popFragment
import com.apkakisan.myapplication.helpers.showShortToast
import com.apkakisan.myapplication.utils.KeyBoardUtil
import com.bumptech.glide.Glide
import com.github.drjacky.imagepicker.ImagePicker
import com.github.drjacky.imagepicker.constant.ImageProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditProfileFragment : BaseFragment() {

    private lateinit var binding: FragmentEditProfileBinding
    private val editProfileViewModel by viewModel<EditProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.ibBack.setOnClickListener {
            popFragment()
        }
        binding.toolbar.tvTitle.text = getString(R.string.edit_profile)

        binding.flPhotoSelector.setOnClickListener {
            ImagePicker.with(activity!!)
                .provider(ImageProvider.BOTH)
                .createIntentFromDialog { launcher.launch(it) }
        }

        binding.btnSave.setOnClickListener {
            editProfileViewModel.validate()
        }

        updateUI()
        initObservers()

        binding.etName.doAfterTextChanged {
            binding.tvNameError.visibility = View.GONE
            editProfileViewModel.name = it.toString().trim()
        }

        binding.etPhoneNo.addTextChangedListener(PhoneNumberFormattingTextWatcher("US"))
        binding.etPhoneNo.doAfterTextChanged {
            binding.tvPhoneError.visibility = View.GONE
            editProfileViewModel.phone = it.toString().trim()
        }
        binding.etAddress.doAfterTextChanged {
            binding.tvAddressError.visibility = View.GONE
            editProfileViewModel.address = it.toString().trim()
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as ProfileActivity).bottomNavigation.visibility = View.GONE
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                it.data?.data?.let { photoUri ->
                    editProfileViewModel.uploadPhoto(photoUri)
                }
            }
        }

    private fun initObservers() {
        lifecycleScope.launch {
            // repeatOnLifecycle launches the block in a new coroutine every time the
            // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                editProfileViewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is EditProfileUiState.PhotoUploading -> {
                            binding.loader.loader.visibility = View.VISIBLE
                        }
                        is EditProfileUiState.PhotoUploadSuccess -> {
                            binding.loader.loader.visibility = View.GONE
                            Glide.with(requireContext())
                                .load(editProfileViewModel.photo)
                                .circleCrop()
                                .into(binding.ivPhoto)
                        }
                        is EditProfileUiState.PhotoUploadFailed -> {
                            binding.loader.loader.visibility = View.GONE
                            showShortToast(getString(R.string.image_upload_failed))
                        }
                        is EditProfileUiState.EmptyName -> {
                            binding.tvNameError.visibility = View.VISIBLE
                            binding.tvNameError.text = getString(R.string.empty_name)
                        }
                        is EditProfileUiState.InvalidPhone -> {
                            binding.tvPhoneError.visibility = View.VISIBLE
                            binding.tvPhoneError.text = getString(R.string.invalid_phone)
                        }
                        is EditProfileUiState.EmptyPhone -> {
                            binding.tvPhoneError.visibility = View.VISIBLE
                            binding.tvPhoneError.text = getString(R.string.empty_phone)
                        }
                        is EditProfileUiState.EmptyAddress -> {
                            binding.tvAddressError.visibility = View.VISIBLE
                            binding.tvAddressError.text = getString(R.string.empty_address)
                        }
                        is EditProfileUiState.DataValidated -> {
                            editProfileViewModel.updateUser()
                        }
                        is EditProfileUiState.ProfileUpdating -> {
                            KeyBoardUtil.hideKeyboard(requireContext(), binding.btnSave)
                            binding.loader.loader.visibility = View.VISIBLE
                        }
                        is EditProfileUiState.ProfileUpdated -> {
                            binding.loader.loader.visibility = View.GONE
                            showAppDialog()
                        }
                    }
                }
            }
        }
    }

    private fun showAppDialog() {
        val dialog = AppDialogFragment.newInstance(
            getString(R.string.profile_updated),
            getString(R.string.profile_updated_successfully)
        )
        dialog.onDonePressed { popFragment() }
        dialog.show(activity?.supportFragmentManager!!, AppDialogFragment.TAG)
    }

    private fun updateUI() {
        CoroutineScope(Dispatchers.Default).launch {
            withContext(Dispatchers.Main) {
                Glide.with(requireContext())
                    .load(editProfileViewModel.photo)
                    .placeholder(R.drawable.logo)
                    .circleCrop()
                    .into(binding.ivPhoto)
                binding.etName.setText(editProfileViewModel.name)
                binding.etPhoneNo.setText(editProfileViewModel.phone)
                binding.etAddress.setText(editProfileViewModel.address)
            }
        }
    }

    companion object {
        const val TAG = "EditProfileFragment"
        fun newInstance() = EditProfileFragment()
    }
}