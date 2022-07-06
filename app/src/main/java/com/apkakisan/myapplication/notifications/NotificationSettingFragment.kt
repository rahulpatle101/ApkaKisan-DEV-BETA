package com.apkakisan.myapplication.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apkakisan.myapplication.BaseFragment
import com.apkakisan.myapplication.R
import com.apkakisan.myapplication.databinding.FragmentNotificationSettingBinding
import com.apkakisan.myapplication.helpers.DefaultItemDecorator
import com.apkakisan.myapplication.helpers.popFragment
import com.apkakisan.myapplication.network.responses.NotificationTypeSetting
import com.apkakisan.myapplication.network.responses.NotificationType
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationSettingFragment : BaseFragment() {

    private val notificationTypeList = mutableListOf<NotificationType>()

    private lateinit var binding: FragmentNotificationSettingBinding
    private val notificationSettingViewModel: NotificationSettingViewModel by viewModel()
    private var adapter: NotificationSettingAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.ibBack.setOnClickListener {
            popFragment()
        }
        binding.toolbar.tvTitle.text = getString(R.string.notification_settings)
        binding.toolbar.ibNotificationSettings.visibility = View.GONE

        binding.rvNotificationSettings.addItemDecoration(DefaultItemDecorator(48, 48))
        adapterProcess()
        getNotificationType()

        notificationSettingViewModel.notificationSettingList.observe(viewLifecycleOwner) {
            it?.let {
                notificationTypeList.addAll(it)
                adapter?.notifyDataSetChanged()
            } ?: showErrorView()
        }
    }

    private fun adapterProcess() {
        if (adapter == null)
            adapter = NotificationSettingAdapter(
                requireContext(),
                notificationTypeList,
                onCheckedClick = { _: NotificationType,
                                   notificationSetting: NotificationTypeSetting,
                                   _: Int ->
                    notificationSettingViewModel.setNotificationTypeState(notificationSetting)
                }
            )
        binding.rvNotificationSettings.adapter = adapter
    }

    private fun getNotificationType() {
        notificationSettingViewModel.getNotificationType()
    }

    companion object {
        const val TAG = "NotificationSettingFragment"
        fun newInstance() = NotificationSettingFragment()
    }
}

