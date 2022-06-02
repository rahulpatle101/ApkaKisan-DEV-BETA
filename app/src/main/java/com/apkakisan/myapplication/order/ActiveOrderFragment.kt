package com.apkakisan.myapplication.order

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apkakisan.myapplication.BaseFragment
import com.apkakisan.myapplication.databinding.FragmentOrderBinding
import com.apkakisan.myapplication.helpers.DefaultItemDecorator
import com.apkakisan.myapplication.helpers.OBJ_ORDER
import com.apkakisan.myapplication.network.responses.Order
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ActiveOrderFragment : BaseFragment() {

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
        val orderReceivedListQuery = reference.orderByChild("orderStatus").equalTo("Received")
        orderReceivedListQuery.addListenerForSingleValueEvent(orderReceivedListener)
    }

    private val orderReceivedListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (orderList.isNotEmpty())
                orderList.clear()
            for (postSnapshot in snapshot.children) {
                val order: Order? = postSnapshot.getValue(Order::class.java)
                order?.let {
                    orderList.add(it)
                }
            }
            val orderConfirmedListQuery = reference.orderByChild("orderStatus").equalTo("Confirmed")
            orderConfirmedListQuery.addListenerForSingleValueEvent(orderConfirmedListener)
        }

        override fun onCancelled(error: DatabaseError) {
            binding.layoutLoader.loader.visibility = View.GONE
            somethingWentWrong()
        }
    }

    private val orderConfirmedListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            for (postSnapshot in snapshot.children) {
                val order: Order? = postSnapshot.getValue(Order::class.java)
                order?.let {
                    orderList.add(it)
                }
            }
            val orderInspectedListQuery = reference.orderByChild("orderStatus").equalTo("Inspected")
            orderInspectedListQuery.addListenerForSingleValueEvent(orderInspectedListener)
        }

        override fun onCancelled(error: DatabaseError) {
            binding.layoutLoader.loader.visibility = View.GONE
            somethingWentWrong()
        }
    }

    private val orderInspectedListener = object : ValueEventListener {
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
                onDetailsBtnClick = { order: Order, _: View, _: Int ->
                    val intent = Intent(activity, OrderDetailActivity::class.java)
                    intent.putExtra(OBJ_ORDER, order)
                    startActivity(intent)
                },
                onSellAgainBtnClick = { _: Order, _: View, _: Int -> }
            )
        binding.rvOrders.adapter = adapter
    }

    companion object {
        val TAG: String = ActiveOrderFragment::class.java.simpleName
        fun newInstance() = ActiveOrderFragment()
    }
}