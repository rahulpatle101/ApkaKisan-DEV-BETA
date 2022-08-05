package com.apkakisan.myapplication.order

import androidx.recyclerview.widget.RecyclerView
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.apkakisan.myapplication.network.responses.Commodity
import android.os.Bundle
import com.apkakisan.myapplication.R
import android.content.Intent
import android.view.View
import com.apkakisan.myapplication.network.RetrofitClient
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import com.apkakisan.myapplication.BaseActivity
import com.apkakisan.myapplication.helpers.DefaultItemDecorator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class HomeActivity : BaseActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    private var commodityItemList: MutableList<Commodity> = ArrayList()

    private lateinit var adapter: CommoditiesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        recyclerView = findViewById(R.id.recycler_view)
        progressBar = findViewById(R.id.progressBar)

        val searchView = findViewById<SearchView>(R.id.searchView)

        bottomNavigation = findViewById(R.id.layoutBottom)
        bottomNavigation.selectedItemId = R.id.home
        setupBottomNavigation()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                filter(s)
                return true
            }
        })

        adapterProcess()
        fetchCommodities()
    }

    private fun adapterProcess() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DefaultItemDecorator(48, 48))
        adapter = CommoditiesAdapter(
            commodityItemList,
            onClick = { commodity: Commodity, _: View, _: Int ->
                startActivity(Intent(this, CreateOrderActivity::class.java).apply {
                    putExtra(CreateOrderActivity.TITLE, commodity.title)
                    putExtra(CreateOrderActivity.MANDI_PRICE, commodity.mandiPrice)
                    putExtra(CreateOrderActivity.APKAKISAN_PRICE, commodity.apkakisanPrice)
                })
            }
        )
        recyclerView.adapter = adapter
    }

    private fun fetchCommodities() {
        progressBar.visibility = View.VISIBLE
        RetrofitClient.getRetrofitClient().commodity_title.enqueue(
            object : Callback<List<Commodity>?> {
                override fun onResponse(
                    call: Call<List<Commodity>?>,
                    response: Response<List<Commodity>?>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        commodityItemList.addAll(response.body() ?: listOf())
                        adapter.notifyDataSetChanged()
                        progressBar.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<List<Commodity>?>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@HomeActivity, "Error: " + t.message, Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

    fun filter(s: String) {
        val filteredList: MutableList<Commodity> = ArrayList()
        for (item in commodityItemList) {
            if (item.title.lowercase().contains(s.lowercase())) {
                filteredList.add(item)
            }
        }
        adapter.filterList(filteredList)
    }
}