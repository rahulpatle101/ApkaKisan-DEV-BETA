package com.apkakisan.myapplication.order

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.apkakisan.myapplication.network.responses.Commodity
import android.os.Bundle
import com.apkakisan.myapplication.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import android.content.Intent
import android.view.View
import com.apkakisan.myapplication.NotificationsActivity
import com.apkakisan.myapplication.ProfileActivity
import com.apkakisan.myapplication.network.RetrofitClient
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import com.apkakisan.myapplication.helpers.DefaultItemDecorator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class HomeActivity : AppCompatActivity() {
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

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.home
        bottomNavigationView.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> return@OnItemSelectedListener true
                R.id.orders -> {
                    startActivity(Intent(applicationContext, OrdersActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@OnItemSelectedListener true
                }
                R.id.notifications -> {
                    startActivity(Intent(applicationContext, NotificationsActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@OnItemSelectedListener true
                }
                R.id.profile -> {
                    startActivity(Intent(applicationContext, ProfileActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@OnItemSelectedListener true
                }
            }
            false
        })

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
                val intent = Intent(this@HomeActivity, CreateOrderActivity::class.java)
                intent.putExtra("commodity_name", commodity.title)
                intent.putExtra(
                    "commodity_apka_kisan_price",
                    commodity.apkakisan_price
                )
                startActivity(intent)
            }
        )
        recyclerView.adapter = adapter
    }

    private fun fetchCommodities() {
        progressBar.visibility = View.VISIBLE
        RetrofitClient.getRetrofitClient().commodity_title.enqueue(object :
            Callback<List<Commodity>?> {
            override fun onResponse(
                call: Call<List<Commodity>?>,
                response: Response<List<Commodity>?>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    commodityItemList.addAll(response.body()!!)
                    adapter.notifyDataSetChanged()
                    progressBar.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<List<Commodity>?>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@HomeActivity, "Error: " + t.message, Toast.LENGTH_SHORT).show()
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