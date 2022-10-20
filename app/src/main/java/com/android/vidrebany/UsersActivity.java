package com.android.vidrebany;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.android.vidrebany.adapters.AdapterUsers;
import com.android.vidrebany.models.ModelUsers;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class UsersActivity extends AppCompatActivity {

    RecyclerView usersRecyclerView;
    AdapterUsers adapterUsers;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar_main);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Escollir usuari:");


        usersRecyclerView = findViewById(R.id.usersRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        usersRecyclerView.setLayoutManager(layoutManager);

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
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}