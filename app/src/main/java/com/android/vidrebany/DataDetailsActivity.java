package com.android.vidrebany;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.android.vidrebany.adapters.AdapterDatesDetails;
import com.android.vidrebany.models.ModelDatesDetails;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class DataDetailsActivity extends AppCompatActivity {

    RecyclerView detailsRecyclerView;
    AdapterDatesDetails adapterDatesDetails;
    TextView dateTv,nameTv, processTv, numberTv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_details);

        nameTv = findViewById(R.id.nameTv);
        processTv = findViewById(R.id.processTv);
        numberTv = findViewById(R.id.numberTv);
        dateTv = findViewById(R.id.dateTv);


        String name = null, process = null, number = null, date = null;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                Intent intent = new Intent(DataDetailsActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                name = extras.getString("name");
                process = extras.getString("process");
                number = extras.getString("number");
                date = extras.getString("date");
                setContent(date,name, process, number);
            }
        } else {
            name = (String) savedInstanceState.getSerializable("name");
            process = (String) savedInstanceState.getSerializable("process");
            number = (String) savedInstanceState.getSerializable("number");
            date = (String) savedInstanceState.getSerializable("date");
            setContent(date,name, process, number);
        }

        number = numberTv.getText().toString();


        detailsRecyclerView = findViewById(R.id.detailsRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        detailsRecyclerView.setLayoutManager(layoutManager);

        assert date != null;
        FirebaseRecyclerOptions<ModelDatesDetails> options = new FirebaseRecyclerOptions.Builder<ModelDatesDetails>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("users").child(number).child("orders").child(date).child(date), ModelDatesDetails.class)
                .build();
        adapterDatesDetails = new AdapterDatesDetails(options, process, number, date);
        detailsRecyclerView.setAdapter(adapterDatesDetails);

    }

    private void setContent(String date, String name, String process, String number) {
        nameTv.setText(name);
        processTv.setText(process);
        numberTv.setText(number);
        dateTv.setText(date);
    }
    @Override
    protected void onStart() {
        super.onStart();
        adapterDatesDetails.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapterDatesDetails.stopListening();
    }
}