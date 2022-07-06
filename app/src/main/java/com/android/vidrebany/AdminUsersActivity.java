package com.android.vidrebany;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.vidrebany.adapters.AdapterAdmin;
import com.android.vidrebany.models.ModelUsers;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AdminUsersActivity extends AppCompatActivity {

    RecyclerView usersRecyclerView;
    AdapterAdmin adapterAdmin;
    Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminusers);


        usersRecyclerView = findViewById(R.id.usersRecyclerView);
        addBtn = findViewById(R.id.addBtn);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);


        usersRecyclerView.setLayoutManager(layoutManager);
        Calendar c = Calendar.getInstance();
        FirebaseRecyclerOptions<ModelUsers> options = new FirebaseRecyclerOptions.Builder<ModelUsers>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("users"), ModelUsers.class)
                .build();
        adapterAdmin = new AdapterAdmin(options);
        usersRecyclerView.setAdapter(adapterAdmin);


   /*     String role;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                role = extras.getString("role");
            }
        } else {
            role= (String) savedInstanceState.getSerializable("role");
        }*/
    }
        @Override
        protected void onStart() {
            super.onStart();
            adapterAdmin.startListening();
        }

        @Override
        protected void onStop() {
            super.onStop();
            adapterAdmin.stopListening();
        }


}