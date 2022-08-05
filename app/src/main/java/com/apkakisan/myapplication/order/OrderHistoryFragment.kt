package com.apkakisan.myapplication.order

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apkakisan.myapplication.BaseFragment
import com.apkakisan.myapplication.R
import com.apkakisan.myapplication.databinding.FragmentOrderBinding
import com.apkakisan.myapplication.helpers.DefaultItemDecorator
import com.apkakisan.myapplication.helpers.LocalStore
import com.apkakisan.myapplication.helpers.showShortToast
import com.apkakisan.myapplication.network.responses.Order
import com.apkakisan.myapplication.utils.StringUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OrderHistoryFragment : BaseFragment() {

    private lateinit var binding: FragmentOrderBinding

    private val orderList = mutableListOf<Order>()

    private var adapter: OrderAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvOrders.addItemDecoration(DefaultItemDecorator(48, 48))
    }

    override fun onResume() {
        super.onResume()
        adapterProcess()
        getOrderFromFirebase()
    }

    private fun adapterProcess() {
        if (adapter == null)
            adapter = OrderAdapter(
                requireContext(),
                TAG,
                orderList,
                onDetailsBtnClick = { _: Order, _: View, _: Int -> },
                onSellAgainBtnClick = { order: Order, _: View, _: Int ->
                    startActivity(Intent(activity, CreateOrderActivity::class.java).apply {
                        putExtra(CreateOrderActivity.TITLE, order.name)
                        putExtra(CreateOrderActivity.MANDI_PRICE, order.mandiRate)
                        putExtra(CreateOrderActivity.APKAKISAN_PRICE, order.apkakisanRate)
                    })
                }
            )
        binding.rvOrders.adapter = adapter
    }

    private fun getOrderFromFirebase() {
        binding.layoutLoader.loader.visibility = View.VISIBLE
        FirebaseDatabase.getInstance().getReference("Orders")
            .orderByChild("userId").equalTo(LocalStore.getUser()?.userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    binding.layoutLoader.loader.visibility = View.GONE
                    orderList.clear()
                    for (postSnapshot in snapshot.children) {
                        val order: Order? = postSnapshot.getValue(Order::class.java)
                        order?.let {
                            if (it.orderStatus == "Completed"
                                || it.orderStatus == "Cancelled"
                            )
                                orderList.add(it)
                        }
                    }
                    adapter?.notifyDataSetChanged()
                    if (orderList.isEmpty() && isVisible)
                        showEmptyView()
                }

                override fun onCancelled(error: DatabaseError) {
                    binding.layoutLoader.loader.visibility = View.GONE
                    showErrorView()
                }
            })
    }

    override fun showLoadingView() {
        binding.layoutLoader.loader.visibility = View.VISIBLE
        binding.tvNoOrders.visibility = View.GONE
    }

    override fun showContentView() {
        binding.layoutLoader.loader.visibility = View.GONE
        binding.tvNoOrders.visibility = View.GONE
    }

    override fun showEmptyView() {
        formatNoOrderString()
        binding.tvNoOrders.visibility = View.VISIBLE
        binding.layoutLoader.loader.visibility = View.GONE
    }

    private fun formatNoOrderString() {
        val spannableString = SpannableString(getString(R.string.no_orders_history))
        val startIndex = StringUtil.findStartIndex(
            getString(R.string.no_orders_history),
            getString(R.string.home)
        )
        val endIndex = StringUtil.findEndIndex(
            startIndex,
            getString(R.string.home)
        )
        spannableString.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                widget.invalidate()
                startHomeActivity()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(
            ForegroundColorSpan(R.color.blue),
            startIndex,
            endIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvNoOrders.text = spannableString
        binding.tvNoOrders.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun startHomeActivity() {
        val intent = Intent(requireContext(), HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        activity?.overridePendingTransition(0, 0)
        activity?.finish()
    }

    companion object {
        val TAG: String = OrderHistoryFragment::class.java.simpleName
        fun newInstance() = OrderHistoryFragment()
    }
}