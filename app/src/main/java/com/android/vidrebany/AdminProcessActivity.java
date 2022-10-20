package com.android.vidrebany;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.vidrebany.adapters.*;
import com.android.vidrebany.models.ModelDates;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.text.TextUtils.isEmpty;

public class AdminProcessActivity extends AppCompatActivity {

    private String process;
    private RecyclerView datesRecyclerView;
    private AdapterProcessDates adapterProcessDates;
    private List<ModelDates> datesList;
    private TextView ordersTotalTv;
    private int sum = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_process);
        ordersTotalTv = findViewById(R.id.ordersTotalTv);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            process = extras.getString("process");
        }

        //Actionbar and its title
        Toolbar toolbar = findViewById(R.id.toolbar_main);


        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Dates "+process);

        //home fragment transaction (default, on start)
        toolbar.setTitle("Dates "+process); //change actionbar title

        datesRecyclerView = findViewById(R.id.datesRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        datesRecyclerView.setLayoutManager(layoutManager);

        datesList = new ArrayList<>();

        loadDates();


        DatabaseReference processesRef = FirebaseDatabase.getInstance().getReference().child("processes").child(process);
        processesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    for (DataSnapshot data1: data.getChildren()) {
                        sum += data1.getChildrenCount();
                    }

                }
                ordersTotalTv.setText("Total ordres: "+sum);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    private void loadDates() {
        DatabaseReference datesRef = FirebaseDatabase.getInstance().getReference().child("processes").child(process);
        datesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                datesList.clear();

                for (DataSnapshot ds: snapshot.getChildren()) {
                    ModelDates modelDates = ds.getValue(ModelDates.class);
                    if (modelDates != null) {
                        datesList.add(modelDates);
                        adapterProcessDates = new AdapterProcessDates(AdminProcessActivity.this, datesList, process);
                        adapterProcessDates.setHasStableIds(true);
                        datesRecyclerView.setAdapter(adapterProcessDates);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (!isEmpty(s)) {
                    searchDates(s);
                } else {
                    loadDates();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (!isEmpty(s)) {
                    searchDates(s);
                } else {
                    loadDates();
                }
                return false;
            }
        });
        return true;
    }

    private void searchDates(String s) {
        DatabaseReference datesRef = FirebaseDatabase.getInstance().getReference().child("processes").child(process);
        datesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                datesList.clear();

                for (DataSnapshot ds: snapshot.getChildren()) {
                    ModelDates modelDates = ds.getValue(ModelDates.class);
                    if (modelDates != null) {
                        String date = modelDates.getDate();
                        if (date.contains(s)) {
                            datesList.add(modelDates);
                        }
                        adapterProcessDates = new AdapterProcessDates(AdminProcessActivity.this, datesList, process);
                        adapterProcessDates.setHasStableIds(true);
                        datesRecyclerView.setAdapter(adapterProcessDates);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                loadDates();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}