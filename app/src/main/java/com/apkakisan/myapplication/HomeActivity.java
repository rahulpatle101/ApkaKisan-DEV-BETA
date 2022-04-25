package com.apkakisan.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements CommoditiesAdapter.OnCommodityListener{
    RecyclerView recyclerView;
    ProgressBar progressBar;
    LinearLayoutManager layoutManager;
    CommoditiesAdapter adapter;
    List<CommodityItem> commodityItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progressBar);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CommoditiesAdapter(commodityItemList, this);
        recyclerView.setAdapter(adapter);

        SearchView searchView = findViewById(R.id.searchView);


        fetchCommodities();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
//                        Toast.makeText(MainContainer.this, "HOME SELECTED", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.orders:
//                        Toast.makeText(MainContainer.this, "ORDER SELECTED", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), OrdersActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.notifications:
//                        Toast.makeText(MainContainer.this, "NOTIFICATION SELECTED", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), NotificationsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
//                        Toast.makeText(MainContainer.this, "PROFILE SELECTED", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
//                Toast.makeText(HomeActivity.this, "text is- "+s, Toast.LENGTH_SHORT).show();
//                System.out.println("adapterrrrrr--> "+adapter);
                filter(s);
                return true;
            }
        });

    }

    public void filter(String s) {
        List<CommodityItem> filteredList = new ArrayList<>();
        for(CommodityItem item : commodityItemList) {
            if(item.getCommodity_title().toLowerCase().contains(s.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
    }


    private void fetchCommodities() {

        progressBar.setVisibility(View.VISIBLE);

        RetrofitClient.getRetrofitClient().getCommodity_title().enqueue(new Callback<List<CommodityItem>>() {
            @Override
            public void onResponse(Call<List<CommodityItem>> call, Response<List<CommodityItem>> response) {
                
                if(response.isSuccessful() && response.body() != null) {
//                    Toast.makeText(HomeActivity.this, "Response body: " + response.body(), Toast.LENGTH_SHORT).show();
//                    System.out.println("here---->>>> "+response.body());
                    commodityItemList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<CommodityItem>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(HomeActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCommodityClick(int position) {
        commodityItemList.get(position);
        Intent intent = new Intent(HomeActivity.this, CreateOrder.class);
        intent.putExtra("commodity_name", commodityItemList.get(position).getCommodity_title());
        intent.putExtra("commodity_apka_kisan_price", commodityItemList.get(position).getCommodity_apkakisan_price());

        startActivity(intent);
    }
}