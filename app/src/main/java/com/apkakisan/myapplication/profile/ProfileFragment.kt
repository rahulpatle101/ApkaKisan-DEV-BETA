package com.apkakisan.myapplication.profile

import android.os.Bundle
import com.apkakisan.myapplication.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apkakisan.myapplication.BaseFragment
import com.apkakisan.myapplication.customerservice.CustomerServiceFragment
import com.apkakisan.myapplication.databinding.FragmentProfileBinding
import com.apkakisan.myapplication.helpers.*
import com.apkakisan.myapplication.order.HomeActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.zeugmasolutions.localehelper.LocaleHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class ProfileFragment : BaseFragment() {

    private lateinit var binding: FragmentProfileBinding
    private val profileViewModel by viewModel<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.ibBack.visibility = View.GONE
        binding.toolbar.tvTitle.text = getString(R.string.profile)

        binding.clEditProfile.setOnClickListener {
            replaceFragment(
                EditProfileFragment.newInstance(),
                true,
                EditProfileFragment.TAG
            )
        }

        binding.clCustomerService.setOnClickListener {
            replaceFragment(
                CustomerServiceFragment.newInstance(),
                true,
                CustomerServiceFragment.TAG
            )
        }

        binding.clLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            it.visibility = View.GONE
        }

        binding.btnEnglish.setOnClickListener {
            // (activity as HomeActivity).updateLocale(Locale("ar"))
            LocaleHelper.setLocale(requireContext(), Locale("en"))
            // LocalStore.isLanguageChanged = true
            (activity as ProfileActivity).restartHomeActivity()
        }

        binding.btnHindi.setOnClickListener {
            // (activity as HomeActivity).updateLocale(Locale("en"))
            LocaleHelper.setLocale(requireContext(), Locale("hi"))
            // LocalStore.isLanguageChanged = true
            (activity as ProfileActivity).restartHomeActivity()
        }

        updateUI()
    }

    override fun onResume() {
        super.onResume()
        (activity as ProfileActivity).bottomNavigation.visibility = View.VISIBLE
    }

    private fun updateUI() {
        CoroutineScope(Dispatchers.Default).launch {
            withContext(Dispatchers.Main) {
                Glide.with(requireContext())
                    .load(profileViewModel.user.photo)
                    .placeholder(R.drawable.logo)
                    .circleCrop()
                    .into(binding.ivPhoto)
                binding.tvName.text = profileViewModel.user.fullName
                binding.tvPhone.text = profileViewModel.getFormattedPhone()
            }
        }
    }

    companion object {
        const val TAG = "ProfileFragment"
        fun newInstance() = ProfileFragment()
    }
}