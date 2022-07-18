package com.apkakisan.myapplication.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.apkakisan.myapplication.BaseFragment
import com.apkakisan.myapplication.R
import com.apkakisan.myapplication.databinding.FragmentNotificationBinding
import com.apkakisan.myapplication.helpers.DefaultItemDecorator
import com.apkakisan.myapplication.helpers.replaceFragment
import com.apkakisan.myapplication.network.responses.Notification
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationFragment : BaseFragment() {

    private val notificationList = mutableListOf<Notification>()

    private lateinit var binding: FragmentNotificationBinding
    private val notificationViewModel: NotificationViewModel by viewModel()
    private var adapter: NotificationAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.ibBack.visibility = View.GONE
        binding.toolbar.tvTitle.text = getString(R.string.notification)
        binding.toolbar.ibNotificationSettings.apply {
            visibility = View.VISIBLE
            setOnClickListener {
                (activity as AppCompatActivity).replaceFragment(
                    NotificationSettingFragment.newInstance(),
                    true,
                    NotificationSettingFragment.TAG
                )
            }
        }

        binding.rvNotifications.addItemDecoration(DefaultItemDecorator(48, 48))
        adapterProcess()
        getNotifications()

        notificationViewModel.notificationList.observe(viewLifecycleOwner) {
            it?.let {
                notificationList.clear()
                notificationList.addAll(it)
                adapter?.notifyDataSetChanged()
                if (notificationList.isEmpty()) showEmptyView() else showContentView()
            } ?: showErrorView()
        }
    }

    override fun showContentView() {
        binding.loader.loader.visibility = View.GONE
        binding.tvEmptyNotifications.visibility = View.GONE
    }

    override fun showEmptyView() {
        binding.loader.loader.visibility = View.GONE
        binding.tvEmptyNotifications.visibility = View.VISIBLE
    }

    private fun adapterProcess() {
        if (adapter == null)
            adapter = NotificationAdapter(
                requireContext(),
                notificationList,
                onNotificationClick = { _: Notification, _: View, _: Int -> }
            )
        binding.rvNotifications.adapter = adapter
    }

    private fun getNotifications() {
        binding.loader.loader.visibility = View.VISIBLE
        notificationViewModel.getNotifications()
    }

    companion object {
        const val TAG = "NotificationFragment"
        fun newInstance() = NotificationFragment()
    }
}

