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
import com.android.vidrebany.adapters.AdapterProcessDateDetails;
import com.android.vidrebany.models.ModelDatesDetails;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import static android.text.TextUtils.isEmpty;

public class ProcessDateDetailsActivity extends AppCompatActivity {

    private TextView processTv, puntuacioTv;
    private RecyclerView detailsRecyclerView;
    private AdapterProcessDateDetails adapterProcessDateDetails;
    private List<ModelDatesDetails> datesDetailsList;
    private String process = "s/p";
    private String date = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_date_details);

        processTv = findViewById(R.id.processTv);
        puntuacioTv = findViewById(R.id.puntuacioTv);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                Intent intent = new Intent(ProcessDateDetailsActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                process = extras.getString("process");
                date = extras.getString("date");
                String puntuacio = extras.getString("puntuacio");
                Objects.requireNonNull(getSupportActionBar()).setTitle(date);
                toolbar.setTitle(date); //change actionbar title
                setContent(process, puntuacio);
            }
        } else {
            process = (String) savedInstanceState.getSerializable("process");
            date = (String) savedInstanceState.getSerializable("date");
        }



        detailsRecyclerView = findViewById(R.id.detailsRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        detailsRecyclerView.setLayoutManager(layoutManager);

        datesDetailsList = new ArrayList<>();

        loadDatesDetails();

    }

    private void loadDatesDetails() {
        DatabaseReference datesDetailsRef = FirebaseDatabase.getInstance().getReference().child("processes").child(process).child(date).child(date);
        datesDetailsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                datesDetailsList.clear();

                for (DataSnapshot ds: snapshot.getChildren()) {
                    ModelDatesDetails modelDatesDetails = ds.getValue(ModelDatesDetails.class);
                    if (modelDatesDetails != null) {
                        datesDetailsList.add(modelDatesDetails);
                        adapterProcessDateDetails = new AdapterProcessDateDetails(ProcessDateDetailsActivity.this, datesDetailsList, process, date);
                        adapterProcessDateDetails.setHasStableIds(true);
                        detailsRecyclerView.setAdapter(adapterProcessDateDetails);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void setContent(String process, String puntuacio) {

        processTv.setText(process);
        puntuacioTv.setText(puntuacio);
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
                    searchDatesDetails(s);
                } else {
                    loadDatesDetails();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (!isEmpty(s)) {
                    searchDatesDetails(s);
                } else {
                    loadDatesDetails();
                }
                return false;            }
        });
        return true;
    }

    private void searchDatesDetails(String s) {
        DatabaseReference datesDetailsRef = FirebaseDatabase.getInstance().getReference().child("processes").child(process).child(date).child(date);
        datesDetailsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                datesDetailsList.clear();

                for (DataSnapshot ds: snapshot.getChildren()) {
                    ModelDatesDetails modelDatesDetails = ds.getValue(ModelDatesDetails.class);
                    if (modelDatesDetails != null) {
                        String code = modelDatesDetails.getCode();
                        String started = modelDatesDetails.getStarted();
                        String ended = modelDatesDetails.getEnded();
                        String user = modelDatesDetails.getUser();
                        if (code.contains(s) || started.contains(s) || ended.contains(s) || user.contains(s)) {
                            datesDetailsList.add(modelDatesDetails);
                        }
                        adapterProcessDateDetails = new AdapterProcessDateDetails(ProcessDateDetailsActivity.this, datesDetailsList, process, date);
                        adapterProcessDateDetails.setHasStableIds(true);
                        detailsRecyclerView.setAdapter(adapterProcessDateDetails);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                loadDatesDetails();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}