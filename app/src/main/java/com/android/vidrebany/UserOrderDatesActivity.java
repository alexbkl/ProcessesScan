package com.android.vidrebany;

import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.android.vidrebany.adapters.AdapterDates;
import com.android.vidrebany.models.ModelDates;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.text.TextUtils.isEmpty;

public class UserOrderDatesActivity extends AppCompatActivity {

    private RecyclerView datesRecyclerView;
    private AdapterDates adapterDates;
    private TextView nameTv, processTv, numberTv;
    private List<ModelDates> datesList;
    private String name, process, number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order_dates);

        nameTv = findViewById(R.id.nameTv);
        processTv = findViewById(R.id.processTv);
        numberTv = findViewById(R.id.numberTv);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                Intent intent = new Intent(UserOrderDatesActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                name = extras.getString("name");
                process = extras.getString("process");
                number = extras.getString("number");
                setContent();
            }
        } else {
            name = (String) savedInstanceState.getSerializable("name");
            process = (String) savedInstanceState.getSerializable("process");
            number = (String) savedInstanceState.getSerializable("number");
            setContent();
        }

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Dates d'ordres");
        toolbar.setTitle("Dates d'ordres"); //change actionbar title

        number = numberTv.getText().toString();



        datesRecyclerView = findViewById(R.id.datesRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        datesRecyclerView.setLayoutManager(layoutManager);

        datesList = new ArrayList<>();

        loadDates();






    }

    private void loadDates() {
        DatabaseReference datesRef = FirebaseDatabase.getInstance().getReference().child("users").child(number).child("orders");
        datesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                datesList.clear();

                for (DataSnapshot ds: snapshot.getChildren()) {
                    ModelDates modelDates = ds.getValue(ModelDates.class);
                    assert modelDates != null;
                    datesList.add(modelDates);
                    adapterDates = new AdapterDates(UserOrderDatesActivity.this, datesList, process, name, number);
                    adapterDates.setHasStableIds(true);
                    datesRecyclerView.setAdapter(adapterDates);
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
        DatabaseReference datesRef = FirebaseDatabase.getInstance().getReference().child("users").child(number).child("orders");
        datesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                datesList.clear();

                for (DataSnapshot ds: snapshot.getChildren()) {
                    ModelDates modelDates = ds.getValue(ModelDates.class);
                    assert modelDates != null;
                    String date = modelDates.getDate();
                    if (date.contains(s)) {
                        datesList.add(modelDates);
                    }
                    adapterDates = new AdapterDates(UserOrderDatesActivity.this, datesList, process, name, number);
                    adapterDates.setHasStableIds(true);
                    datesRecyclerView.setAdapter(adapterDates);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                loadDates();
            }
        });
    }

    private void setContent() {
        nameTv.setText(name);
        processTv.setText(process);
        numberTv.setText(number);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}