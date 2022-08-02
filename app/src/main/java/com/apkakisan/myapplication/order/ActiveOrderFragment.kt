package com.apkakisan.myapplication.order

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apkakisan.myapplication.BaseFragment
import com.apkakisan.myapplication.databinding.FragmentOrderBinding
import com.apkakisan.myapplication.helpers.DefaultItemDecorator
import com.apkakisan.myapplication.helpers.LocalStore
import com.apkakisan.myapplication.helpers.OBJ_ORDER
import com.apkakisan.myapplication.network.responses.Order
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ActiveOrderFragment : BaseFragment() {

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

    private fun getOrderFromFirebase() {
        binding.layoutLoader.loader.visibility = View.VISIBLE
        FirebaseDatabase.getInstance().getReference("Orders")
            .orderByChild("userId").equalTo(LocalStore.user?.userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    binding.layoutLoader.loader.visibility = View.GONE
                    orderList.clear()
                    for (postSnapshot in snapshot.children) {
                        val order: Order? = postSnapshot.getValue(Order::class.java)
                        order?.let {
                            if (it.orderStatusEn == "Received"
                                || it.orderStatusEn == "Inspected"
                                || it.orderStatusEn == "Confirmed"
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

    override fun showEmptyView() {
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