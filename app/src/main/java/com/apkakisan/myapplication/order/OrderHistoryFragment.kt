package com.apkakisan.myapplication.order

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apkakisan.myapplication.BaseFragment
import com.apkakisan.myapplication.R
import com.apkakisan.myapplication.databinding.FragmentOrderBinding
import com.apkakisan.myapplication.helpers.DefaultItemDecorator
import com.apkakisan.myapplication.helpers.OBJ_ORDER
import com.apkakisan.myapplication.helpers.showShortToast
import com.apkakisan.myapplication.network.responses.Order
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OrderHistoryFragment : BaseFragment() {

    private lateinit var binding: FragmentOrderBinding

    private val orderList = mutableListOf<Order>()

    val reference = FirebaseDatabase.getInstance().getReference("ORDERS")
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

    private fun getOrderFromFirebase() {
        binding.layoutLoader.loader.visibility = View.VISIBLE
        val orderCancelledListQuery = reference.orderByChild("orderStatus").equalTo("Completed")
        orderCancelledListQuery.addListenerForSingleValueEvent(orderCompletedListener)
    }

    private val orderCompletedListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (orderList.isNotEmpty())
                orderList.clear()
            for (postSnapshot in snapshot.children) {
                val order: Order? = postSnapshot.getValue(Order::class.java)
                order?.let {
                    orderList.add(it)
                }
            }
            val orderCancelledListQuery = reference.orderByChild("orderStatus").equalTo("Cancelled")
            orderCancelledListQuery.addListenerForSingleValueEvent(orderCancelledListener)
        }

        override fun onCancelled(error: DatabaseError) {
            binding.layoutLoader.loader.visibility = View.GONE
            somethingWentWrong()
        }
    }

    private val orderCancelledListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            binding.layoutLoader.loader.visibility = View.GONE
            for (postSnapshot in snapshot.children) {
                val order: Order? = postSnapshot.getValue(Order::class.java)
                order?.let {
                    orderList.add(it)
                }
            }
            adapter?.notifyDataSetChanged()
        }

        override fun onCancelled(error: DatabaseError) {
            binding.layoutLoader.loader.visibility = View.GONE
            somethingWentWrong()
        }
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
                        putExtra("commodity_name", order.name)
                        putExtra("mandiPrice", order.mandiRate)
                        putExtra("commodity_apka_kisan_price", order.apkakisanRate)
                    })
                }
            )
        binding.rvOrders.adapter = adapter
    }

    companion object {
        val TAG: String = OrderHistoryFragment::class.java.simpleName
        fun newInstance() = OrderHistoryFragment()
    }
}