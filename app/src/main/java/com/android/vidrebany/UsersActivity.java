package com.android.vidrebany;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.vidrebany.adapters.AdapterUsers;
import com.android.vidrebany.models.ModelUsers;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class UsersActivity extends AppCompatActivity {

    RecyclerView usersRecyclerView;
    AdapterUsers adapterUsers;
    Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usersRecyclerView = findViewById(R.id.usersRecyclerView);
        addBtn = findViewById(R.id.addBtn);
        addBtn.setVisibility(View.GONE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        usersRecyclerView.setLayoutManager(layoutManager);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UsersActivity.this, "Afegir usuari", Toast.LENGTH_SHORT).show();
            }
        });
        FirebaseRecyclerOptions<ModelUsers> options = new FirebaseRecyclerOptions.Builder<ModelUsers>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("users"), ModelUsers.class)
                .build();
            adapterUsers = new AdapterUsers(options);
            usersRecyclerView.setAdapter(adapterUsers);

    }


    @Override
    protected void onStart() {
        super.onStart();
        adapterUsers.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapterUsers.stopListening();
    }


}