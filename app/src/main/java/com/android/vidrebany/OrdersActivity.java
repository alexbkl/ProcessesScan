package com.android.vidrebany;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.android.vidrebany.adapters.AdapterOrders;
import com.android.vidrebany.models.ModelOrders;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class OrdersActivity extends AppCompatActivity {

    RecyclerView ordersRecyclerView;
    AdapterOrders adapterOrders;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        ordersRecyclerView = findViewById(R.id.ordersRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        ordersRecyclerView.setLayoutManager(layoutManager);


    }


}